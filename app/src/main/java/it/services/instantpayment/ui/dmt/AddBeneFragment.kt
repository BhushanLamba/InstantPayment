package it.services.instantpayment.ui.dmt

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentAddBeneBinding
import it.services.instantpayment.models.BankModel
import it.services.instantpayment.repository.AddBeneRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.dmt.addBene.AddBeneViewModel
import it.services.instantpayment.viewModels.dmt.addBene.AddBeneViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject


class AddBeneFragment : Fragment() {

    private lateinit var binding: FragmentAddBeneBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var addBeneViewModel: AddBeneViewModel
    private lateinit var progressDialog: AlertDialog

    private lateinit var bankList: ArrayList<BankModel>

    private var bankName: String = ""
    private var ifsc: String = ""
    private var beneName: String = ""
    private var accountNumber: String = ""
    private var senderMobile: String = ""

    private val addBeneLiveData = MutableLiveData<JSONObject>()
    val addBeneData: LiveData<JSONObject>
        get() = addBeneLiveData


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddBeneBinding.inflate(inflater, container, false)
        context = requireContext();
        activity = requireActivity()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)
        senderMobile = arguments?.getString("senderMobile").toString()

        if (SenderMobileVerificationFragment.sType.equals("UPI", true)) {
            binding.apply {
                tvAccountUpi.text = "UPI ID*"
                etAccountNo.hint = "Enter UPI ID"
                tvIfsc.visibility = View.GONE
                etIfsc.visibility = View.GONE
                btnVerify.visibility = View.GONE

            }
        }
        setUpViewModels()
        setUpObservers()
        handleClicksAndEvents()



        return binding.root
    }

    private fun setUpObservers() {
        addBeneViewModel.bankData.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, response.errMessage, Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    bankList = response.data!!

                }
            }
        }

        addBeneViewModel.addBeneData.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, response.errMessage, Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    addBeneLiveData.postValue(response.data!!)
                    fragmentManager?.popBackStackImmediate()

                }
            }
        }

        addBeneViewModel.verifyBeneData.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, response.errMessage, Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    val beneName= response.data.toString()
                    binding.etName.setText(beneName)

                }
            }
        }
    }

    private fun setUpViewModels() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = AddBeneRepository(webService)
        addBeneViewModel = ViewModelProvider(
            this,
            AddBeneViewModelFactory(repository)
        )[AddBeneViewModel::class.java]

        if (!SenderMobileVerificationFragment.sType.equals("UPI", true)) {
            addBeneViewModel.getBank(MainActivity.LOGIN_SESSION, ApiKeys.BANK_LIST_KEY)
        }
    }

    private fun handleClicksAndEvents() {
        binding.apply {
            etBank.setOnClickListener {
                if (SenderMobileVerificationFragment.sType.equals("UPI", true)) {
                    bankList = ArrayList()
                    val phonePeModel = BankModel("PhonePe", "0", "0")
                    val googlePayModel = BankModel("GooglePay", "0", "0")
                    val paytmModel = BankModel("Paytm", "0", "0")
                    val amazonPayModel = BankModel("Amazon Pay", "0", "0")

                    bankList.add(phonePeModel)
                    bankList.add(googlePayModel)
                    bankList.add(paytmModel)
                    bankList.add(amazonPayModel)
                }

                val bundle = Bundle()
                bundle.putSerializable("bankList", bankList)
                replaceFragment(BankFragment(), bundle)
            }

            btnProceed.setOnClickListener {
                if (checkValidation()) {
                    addBeneViewModel.addBene(
                        MainActivity.LOGIN_SESSION,
                        ApiKeys.ADD_BENE_KEY,
                        senderMobile,
                        ifsc,
                        accountNumber,
                        bankName,
                        beneName
                    )
                }
            }

            btnVerify.setOnClickListener {
                if (checkValidation()) {
                    addBeneViewModel.verifyBene(
                        MainActivity.LOGIN_SESSION,
                        ApiKeys.ADD_BENE_KEY,
                        senderMobile,
                        ifsc,
                        accountNumber,
                        bankName,
                        beneName
                    )
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        binding.apply {

            ifsc = etIfsc.text.toString().trim()
            beneName = etName.text.toString().trim()
            accountNumber = etAccountNo.text.toString().trim()

            if (bankName.equals("", true)) {
                etBank.requestFocus()
                etBank.error = "Required"
                return false
            } else if (ifsc.equals("", true)) {
                etIfsc.requestFocus()
                etIfsc.error = "Required"
                return false
            } else if (beneName.equals("", true)) {
                etName.requestFocus()
                etName.error = "Required"
                return false
            } else if (accountNumber.equals("", true)) {
                etAccountNo.requestFocus()
                etAccountNo.error = "Required"
                return false
            }
            return true
        }
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle

        if (fragment is BankFragment) {
            fragment.bankData.observe(viewLifecycleOwner)
            { bankModel ->

                bankName = bankModel.bankName
                ifsc = bankModel.ifsc
                binding.etBank.setText(bankName)
                binding.etIfsc.setText(ifsc)
            }
        }

        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }

}