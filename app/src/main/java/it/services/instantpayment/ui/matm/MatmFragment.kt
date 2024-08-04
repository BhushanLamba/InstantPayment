package it.services.instantpayment.ui.matm

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.finopaytech.finosdk.activity.MainTransactionActivity
import com.finopaytech.finosdk.encryption.AES_BC
import com.finopaytech.finosdk.helpers.Utils
import it.services.instantpayment.MainActivity
import it.services.instantpayment.databinding.FragmentMatmBinding
import it.services.instantpayment.repository.MatmRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.matm.MAtmViewModelFactory
import it.services.instantpayment.viewModels.matm.MatmViewModel
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class MatmFragment : Fragment() {

    private lateinit var binding: FragmentMatmBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private var serviceId = "156"
    private lateinit var amount: String
    private lateinit var mobileNumber: String
    private lateinit var merchantId: String
    private lateinit var matmViewModel: MatmViewModel
    private lateinit var progressDialog:AlertDialog
    private lateinit var service:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatmBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()
        //merchantId="8684020633"
        merchantId=MainActivity.MOBILE_NO
        progressDialog=CustomDialogs.getCustomProgressDialog(activity)
        setUpViewModel()
        setUpObserver()
        handleClickEvents()

        return binding.root
    }

    private fun setUpObserver() {
        matmViewModel.mAtmInitiateData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Response.Error ->{
                    progressDialog.dismiss()
                    CustomDialogs.getMessageDialog(activity,it.errMessage.toString(),false)
                }
                is Response.Loading -> {
                    progressDialog.show()
                }
                is Response.Success -> {
                    progressDialog.dismiss()
                    val clientRegId = it.data

                   val requestData = getEncryptedRequest(
                       merchantId, serviceId, "https://www.google.com/", "1000", amount,
                       clientRegId, "680e8aff-6938-4ae1-a197-b981e278069a"
                   )
                   val headerRequest: String =
                       getEncryptedHeader(
                           "9d035089-4edf-4019-8761-67c35490e76f",
                           "225",
                           "982b0d01-b262-4ece-a2a2-45be82212ba1"
                       )

                   val intent = Intent(getActivity(), MainTransactionActivity::class.java)
                   intent.putExtra("RequestData", requestData)
                   intent.putExtra("HeaderData", headerRequest)
                   intent.putExtra("ReturnTime", 5)
                   //startActivity(intent)
                    mAtmActivityLauncher.launch(intent)
                }
            }
        }
    }

    val mAtmActivityLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        val intent=it.data
        val extras=intent?.extras
        Log.d("TAG", "data : "+extras)
    }

    private fun setUpViewModel() {

        val webService=RetrofitClient.getInstance().create(WebService::class.java)
        val repository=MatmRepository(webService)
        matmViewModel=ViewModelProvider(this,MAtmViewModelFactory(repository))[MatmViewModel::class.java]

    }

    private fun handleClickEvents() {
        binding.apply {

            rgService.setOnCheckedChangeListener { _, _ ->
                if (rbCashWithdrawal.isChecked) {
                    amountLy.visibility = View.VISIBLE
                    serviceId = "171"
                } else {
                    amountLy.visibility = View.GONE
                    serviceId = "172"
                    etAmount.setText("")

                }
            }

            btnProceed.setOnClickListener {
                amount = etAmount.text.toString().trim()
                mobileNumber = etNumber.text.toString().trim()

                if (serviceId == "172")
                {
                    amount="0"
                    service="BE"

                }
                else
                {
                    service="CW"
                }

                if (mobileNumber.length != 10) {
                    etNumber.error = "Invalid"
                    return@setOnClickListener
                }
                else if (rbCashWithdrawal.isChecked && TextUtils.isEmpty(amount)) {
                    etAmount.error = "Required"
                    return@setOnClickListener
                }
                else {
                    matmViewModel.initiateMAtmTransaction(amount,service)
                }
            }

        }
    }

    private fun getEncryptedHeader(
        authKey: String?,
        clientId: String?,
        encryptionHeaderKey: String?
    ): String {
        var headerrequest = ""
        val headerdata = JSONObject()
        headerrequest = try {
            headerdata.put("AuthKey", authKey)
            headerdata.put("ClientId", clientId)
            Utils.replaceNewLine(
                AES_BC.getInstance().encryptEncode(headerdata.toString(), encryptionHeaderKey)
            )
        } catch (e: java.lang.Exception) {
            ""
        }
        return headerrequest
    }

    private fun getEncryptedRequest(
        merchantId: String?,
        serviceId: String?,
        returnUrl: String?,
        version: String?,
        amount: String?,
        clientRefID: String?,
        encryptionkey: String?
    ): String? {
        var strRequestData: String? = ""
        val jsonRequestDataObj = JSONObject() // inner object request


        strRequestData = try {
            jsonRequestDataObj.put("MerchantId", merchantId)
            jsonRequestDataObj.put("SERVICEID", serviceId)
            jsonRequestDataObj.put("RETURNURL", returnUrl)
            jsonRequestDataObj.put("Version", version)
            jsonRequestDataObj.put("Amount", amount)
            jsonRequestDataObj.put("ClientRefID", clientRefID)

            Log.d("requestJson", "getEncryptedRequest: $jsonRequestDataObj")
            Utils.replaceNewLine(
                AES_BC.getInstance().encryptEncode(jsonRequestDataObj.toString(), encryptionkey)
            )
        } catch (e: Exception) {
            ""
        }
        return strRequestData
    }

}