package it.services.instantpayment.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.finopaytech.finosdk.encryption.AES_BC
import com.finopaytech.finosdk.helpers.Utils
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentHomeBinding
import it.services.instantpayment.models.User
import it.services.instantpayment.repository.BalanceRepository
import it.services.instantpayment.repository.OperatorRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.ui.addUser.AddUserFragment
import it.services.instantpayment.ui.addUser.AdduserUserTypeFragment
import it.services.instantpayment.utils.ApiKeys.BALANCE_KEY
import it.services.instantpayment.utils.ApiKeys.BBPS_OPERATOR_KEY
import it.services.instantpayment.utils.ApiKeys.OPERATOR_KEY
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.balance.BalanceViewModel
import it.services.instantpayment.viewModels.balance.BalanceViewModelFactory
import it.services.instantpayment.viewModels.operator.OperatorVideoModelFactory
import it.services.instantpayment.viewModels.operator.OperatorViewModel
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var mySliderList: ArrayList<SlideModel>
    private lateinit var binding: FragmentHomeBinding

    private lateinit var operatorViewModel: OperatorViewModel
    private lateinit var balanceViewModel: BalanceViewModel


    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var progressDialog: AlertDialog
    private lateinit var bbpsServiceType: String
    private lateinit var bbpsServiceId: String
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        context = requireContext()
        activity = requireActivity()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)
        navHostFragment=getActivity()?.supportFragmentManager?.findFragmentById(R.id.frame_container) as NavHostFragment
        navController=navHostFragment.navController
        setUpDashBoard()
        setImageSlider()
        setUpViewModel()
        handleClicksAndEvents()
        setObservers()

        return binding.root
    }

    private fun setUpDashBoard() {


        if (!MainActivity.USER_TYPE.equals("RT",true))
        {
            binding.retailerLy.visibility=View.VISIBLE
            binding.distributorLy.visibility=View.GONE
        }
        else
        {
            binding.retailerLy.visibility=View.GONE
            binding.distributorLy.visibility=View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        operatorViewModel.operatorData.observe(viewLifecycleOwner) {

            when (it) {

                is Response.Loading -> {

                    progressDialog.show()

                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    it.data?.let { data ->
                        val bundle = Bundle()
                        bundle.putSerializable("operatorList", data)
                        //replaceFragment(OperatorFragment(), bundle)
                        if (operatorViewModel.isNavigate)
                        navController.navigate(R.id.action_homeFragment_to_operatorFragment,bundle)
                        operatorViewModel.isNavigate=false
                    }
                }

            }
        }

        operatorViewModel.bbpsOperatorData.observe(viewLifecycleOwner)
        {
            when (it) {

                is Response.Loading -> {

                    progressDialog.show()

                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    it.data?.let { data ->
                        val bundle = Bundle()
                        bundle.putSerializable("operatorList", data)
                        bundle.putString("service", bbpsServiceType)
                        bundle.putString("serviceId", bbpsServiceId)
                            //replaceFragment(BbpsBillFetchFragment(), bundle)
                        findNavController().navigate(R.id.action_homeFragment_to_bbpsBillFetchFragment,bundle)
                    }
                }

            }

        }

        balanceViewModel.balanceData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    binding.tvBalance.text = "₹ 00.0"
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    it.data?.let { data ->
                        binding.tvBalance.text = "₹ $data"
                    }
                }
            }
        }

    }

    private fun setUpViewModel() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val operatorRepository = OperatorRepository(webService)
        val balanceRepository = BalanceRepository(webService)

        operatorViewModel = ViewModelProvider(
            this,
            OperatorVideoModelFactory(operatorRepository)
        )[OperatorViewModel::class.java]
        balanceViewModel = ViewModelProvider(
            this,
            BalanceViewModelFactory(balanceRepository)
        )[BalanceViewModel::class.java]


    }


    @SuppressLint("SetTextI18n")
    private fun handleClicksAndEvents() {
        binding.apply {

            //binding.tvMobile.text = "WELCOME\n${MainActivity.MOBILE_NO}"
            //binding.tvName.text = MainActivity.NAME
            binding.user=User(MainActivity.NAME,MainActivity.MOBILE_NO)


            paymentRequestLy.setOnClickListener {

                //replaceFragment(FundRequestFragment(), Bundle())
                findNavController().navigate(R.id.action_homeFragment_to_fundRequestFragment)

            }

            prepaidLy.setOnClickListener {
                if (MainActivity.checkPermission("PREPAID")) {
                    operatorViewModel.isNavigate=true
                    operatorViewModel.getOperator(MainActivity.LOGIN_SESSION, OPERATOR_KEY, "1")
                }
                else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )

            }

            dthLy.setOnClickListener {
                if (MainActivity.checkPermission("DTH")) {
                    operatorViewModel.isNavigate=true
                    operatorViewModel.getOperator(MainActivity.LOGIN_SESSION, OPERATOR_KEY, "2")
                }
                else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )

            }

            tvBalance.setOnClickListener {

                balanceViewModel.getBalance(MainActivity.LOGIN_SESSION, BALANCE_KEY)

            }

            electrcityLy.setOnClickListener {
                if (MainActivity.checkPermission("BILLPAYMENT")) {
                    bbpsServiceType = "Electricity"
                    bbpsServiceId = "3"
                    operatorViewModel.getBbpsOperator(
                        MainActivity.LOGIN_SESSION,
                        BBPS_OPERATOR_KEY,
                        "3"
                    )
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )

            }

            fasTagLy.setOnClickListener {

                if (MainActivity.checkPermission("BILLPAYMENT")) {
                    bbpsServiceType = "FasTag"
                    bbpsServiceId = "4"
                    operatorViewModel.getBbpsOperator(
                        MainActivity.LOGIN_SESSION,
                        BBPS_OPERATOR_KEY,
                        "4"
                    )
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )


            }

            postpaidLy.setOnClickListener {

                if (MainActivity.checkPermission("BILLPAYMENT")) {
                    bbpsServiceType = "Postpaid"
                    bbpsServiceId = "5"
                    operatorViewModel.getBbpsOperator(
                        MainActivity.LOGIN_SESSION,
                        BBPS_OPERATOR_KEY,
                        "5"
                    )
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )


            }

            loanLy.setOnClickListener {

                if (MainActivity.checkPermission("BILLPAYMENT")) {
                    bbpsServiceType = "Loan Repayment"
                    bbpsServiceId = "6"
                    operatorViewModel.getBbpsOperator(
                        MainActivity.LOGIN_SESSION,
                        BBPS_OPERATOR_KEY,
                        "6"
                    )
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )


            }

            licLy.setOnClickListener {

                if (MainActivity.checkPermission("BILLPAYMENT")) {
                    bbpsServiceId = "7"
                    bbpsServiceType = "Life Insurance Co."
                    operatorViewModel.getBbpsOperator(
                        MainActivity.LOGIN_SESSION,
                        BBPS_OPERATOR_KEY,
                        "7"
                    )
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )


            }

            gasLy.setOnClickListener {

                if (MainActivity.checkPermission("BILLPAYMENT")) {
                    bbpsServiceId = "8"
                    bbpsServiceType = "Gas"
                    operatorViewModel.getBbpsOperator(
                        MainActivity.LOGIN_SESSION,
                        BBPS_OPERATOR_KEY,
                        "8"
                    )
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )


            }

            creditCardLy.setOnClickListener {

                if (MainActivity.checkPermission("BILLPAYMENT")) {
                    bbpsServiceId = "9"
                    bbpsServiceType = "Credit Card"
                    operatorViewModel.getBbpsOperator(
                        MainActivity.LOGIN_SESSION,
                        BBPS_OPERATOR_KEY,
                        "9"
                    )
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )


            }

            dmtLy.setOnClickListener {
                //replaceFragment(SenderMobileVerificationFragment(), Bundle())
                findNavController().navigate(R.id.action_homeFragment_to_senderMobileVerificationFragment,Bundle())

            }


            dmtUpiLy.setOnClickListener {

                if (MainActivity.checkPermission("UPI")) {
                    val bundle = Bundle()
                    bundle.putString("serviceType", "UPI")
                    //replaceFragment(SenderMobileVerificationFragment(), bundle)
                    findNavController().navigate(R.id.action_homeFragment_to_senderMobileVerificationFragment,bundle)
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )


            }

            aeps2Ly.setOnClickListener {
                if (MainActivity.checkPermission("AEPS2")) {
                    checkAppInstalledOrNot()
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )

            }

            aeps1Ly.setOnClickListener {
                CustomDialogs.getMessageDialog(
                    activity,
                    "This service is not activated.\nPlease contact admin.",
                    false
                )

                /*if (MainActivity.checkPermission("AEPS2")) {
                    if (MainActivity.IS_AePS_APPROVED.equals("Approved", true)) {
                        AlertDialog.Builder(context)
                            .setMessage("This service will be launching soon.")
                            .setPositiveButton("OK", null)
                            .show()
                    } else {

                        val partnerId = "PS004648"
                        val partnerKey =
                            "UFMwMDQ2NDg2OTZjNmY4ZTc3Mjc3MmVlNGJiOWFkMGQyYWFmMjY3MjE2OTY4NTk3NTk="
                        val intent = Intent(context, HostActivity::class.java)
                        intent.putExtra("pId", partnerId)//partner Id provided in credential
                        intent.putExtra("pApiKey", partnerKey)//JWT API Key provided in credential
                        intent.putExtra("mCode", "IP${MainActivity.MOBILE_NO}")//Merchant Code
                        intent.putExtra("mobile", MainActivity.MOBILE_NO)// merchant mobile number
                        intent.putExtra("lat", "42.10")
                        intent.putExtra("lng", "76.00")
                        intent.putExtra("firm", MainActivity.COMPANY_NAME)
                        intent.putExtra("email", MainActivity.EMAIL_ID)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        aePSOnBoardingLauncher.launch(intent)
                    }
                }

                else
                    CustomDialogs.getMessageDialog(activity,"This service is not activated.\nPlease contact admin.",false)

*/



            }

            razorPayLy.setOnClickListener {
                if (MainActivity.checkPermission("PAYMENT GATEWAY")) {
                    //startActivity(Intent(context, PaymentActivity::class.java))
                    findNavController().navigate(R.id.action_homeFragment_to_paymentActivity)
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )

            }

            upiLy.setOnClickListener {
                if (MainActivity.checkPermission("UPI GATEWAY")) {
                    //replaceFragment(UpiPaymentFragment(), Bundle())
                    findNavController().navigate(R.id.action_homeFragment_to_upiPaymentFragment)
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )

            }

            matmLy.setOnClickListener {

                /*val clientRegId=MainActivity.MOBILE_NO+System.currentTimeMillis()
                
                val requestData = getEncryptedRequest(
                    "8010076222", "156", "https://www.google.com/", "1000", "1",
                    clientRegId, "680e8aff-6938-4ae1-a197-b981e278069a"
                )
                val headerRequest: String =
                    getEncryptedHeader("9d035089-4edf-4019-8761-67c35490e76f", "225", "982b0d01-b262-4ece-a2a2-45be82212ba1")

                val intent = Intent(getActivity(), MainTransactionActivity::class.java)
                intent.putExtra("RequestData", requestData)
                intent.putExtra("HeaderData", headerRequest)
                intent.putExtra("ReturnTime", 5)
                //startActivityForScanDevice_Fino_D80.launch(intent)
                startActivity(intent)*/

                //replaceFragment(MatmFragment(),Bundle())
                findNavController().navigate(R.id.action_homeFragment_to_matmFragment)
            }

            tvTopUp.setOnClickListener {
                if (MainActivity.checkPermission("UPI GATEWAY")) {
                    //replaceFragment(UpiPaymentFragment(), Bundle())
                    findNavController().navigate(R.id.action_homeFragment_to_upiPaymentFragment)
                } else
                    CustomDialogs.getMessageDialog(
                        activity,
                        "This service is not activated.\nPlease contact admin.",
                        false
                    )

            }

            addUserLy.setOnClickListener {
                replaceFragment(AdduserUserTypeFragment(),Bundle())
            }

        }
    }

    private fun getEncryptedHeader(
        authKey: String?,
        clientId: String?,
        encryptionheaderkey: String?
    ): String {
        var headerrequest = ""
        val headerdata = JSONObject()
        headerrequest = try {
            headerdata.put("AuthKey", authKey)
            headerdata.put("ClientId", clientId)
            Utils.replaceNewLine(
                AES_BC.getInstance().encryptEncode(headerdata.toString(), encryptionheaderkey)
            )
        } catch (e: java.lang.Exception) {
            ""
        }
        return headerrequest
    }

    fun getEncryptedRequest(
        merchantId: String?,
        serviceid: String?,
        returnurl: String?,
        version: String?,
        amount: String?,
        clientRefID: String?,
        encryptionkey: String?
    ): String? {
        var strRequestData: String? = ""
        val jsonRequestDataObj = JSONObject() // inner object request
        strRequestData = try {
            jsonRequestDataObj.put("MerchantId", merchantId)
            jsonRequestDataObj.put("SERVICEID", serviceid)
            jsonRequestDataObj.put("RETURNURL", returnurl)
            jsonRequestDataObj.put("Version", version)
            jsonRequestDataObj.put("Amount", amount)
            jsonRequestDataObj.put("ClientRefID", clientRefID)
            Utils.replaceNewLine(
                AES_BC.getInstance().encryptEncode(jsonRequestDataObj.toString(), encryptionkey)
            )
        } catch (e: java.lang.Exception) {
            ""
        }
        return strRequestData
    }


    private fun checkAppInstalledOrNot() {
        val intalled = appInstalledOrNot("com.aeps.aeps_api_user_production")
        try {
            if (intalled) {
                //replaceFragment(AePSServiceFragment(),Bundle())
                findNavController().navigate(R.id.action_homeFragment_to_aePSServiceFragment)
            } else {
                showAlert()
            }
        } catch (ignore: Exception) {
            showAlert()
        }
    }

    class DataModel {
        var applicationType: String? = null
        var userNameFromCoreApp: String? = null
        var clientRefID: String? = null
        var clientID: String? = null
        var clientSecret: String? = null
        var paramB: String? = null
        var paramC: String? = null
        var API_USER_NAME_VALUE: String? = null
        var SHOP_NAME: String? = null
        var BRAND_NAME: String? = null
        var location: String? = null
        var agent: String? = null
        var skipReceipt: Boolean? = null
        var transactionAmount: String? = null
        var transactionType: String? = null
        var bankCode: String? = null

        override fun toString(): String {
            return super.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == 80) {
                var transactionData = ""
                var clientRefId = ""
                if (data.hasExtra("data")) {
                    transactionData = data.getStringExtra("data").toString()
                }
                if (data.hasExtra("clientRefId")) {
                    clientRefId = data.getStringExtra("clientRefId").toString()
                }
            }
        }
    }

    private fun showAlert() {
        try {
            val alertbuilderupdate: AlertDialog.Builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert)
            alertbuilderupdate.setCancelable(false)
            val message = "Please download the AEPS SERVICE app ."
            alertbuilderupdate.setTitle("Alert")
                .setMessage(message)
                .setPositiveButton(
                    "Download Now"
                ) { _, _ -> redirectToAppStore() }
                .setNegativeButton(
                    "Not Now"
                ) { dialog, _ -> dialog.dismiss() }
            val alert11 = alertbuilderupdate.create()
            alert11.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun redirectToAppStore() {
        val uri = Uri.parse("https://liveappstore.in/shareapp?com.aeps.aeps_api_user_production=")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket);
        } catch (e: Exception) {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://liveappstore.in/shareapp?com.aeps.aeps_api_user_production=")
                    )
                )
            } catch (exception: Exception) {
                Toast.makeText(context, "Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm: PackageManager = requireActivity().packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }

    private val aePSOnBoardingLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        val data = it.data
        val message = data?.getStringExtra("message")
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun setImageSlider() {
        mySliderList = ArrayList()
        mySliderList.add(SlideModel(it.services.instantpayment.R.drawable.slider1, ScaleTypes.FIT))
        mySliderList.add(SlideModel(it.services.instantpayment.R.drawable.slider2, ScaleTypes.FIT))

        binding.imageSlider.setImageList(mySliderList, ScaleTypes.FIT)
    }

    private fun replaceFragment(fragment: Fragment, bundle:   Bundle) {
        fragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(it.services.instantpayment.R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

}