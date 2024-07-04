package it.services.instantpayment.ui.dmt

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import it.services.instantpayment.MainActivity
import it.services.instantpayment.R
import it.services.instantpayment.databinding.FragmentAddSenderBinding
import it.services.instantpayment.repository.AddSenderRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.repository.SenderVerificationRepository
import it.services.instantpayment.ui.login.LoginActivity
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.dmt.addSender.AddSenderViewModel
import it.services.instantpayment.viewModels.dmt.addSender.AddSenderViewModelFactory
import it.services.instantpayment.viewModels.dmt.senderVerification.SenderMobileVerificationViewModel
import it.services.instantpayment.viewModels.dmt.senderVerification.SenderMobileVerificationViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import java.lang.Exception


class AddSenderFragment : Fragment() {

    private lateinit var binding: FragmentAddSenderBinding
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var fName: String
    private lateinit var lName: String
    private lateinit var pinCode: String
    private lateinit var address: String
    private lateinit var mobileNumber: String
    private lateinit var otp: String
    private lateinit var state:String

    private lateinit var addSenderViewModel: AddSenderViewModel
    private lateinit var verifySenderViewModel: SenderMobileVerificationViewModel
    private lateinit var progressDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddSenderBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()
        mobileNumber = arguments?.getString("number", "").toString()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)
        setUpViewModels()
        setUpViewObservers()
        handleClickAndEvents()
        return binding.root
    }

    private fun setUpViewObservers() {
        addSenderViewModel.addSenderData.observe(viewLifecycleOwner)
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
//                    fragmentManager?.popBackStackImmediate()
//                    verifySenderViewModel.verifySender(
//                        MainActivity.LOGIN_SESSION,
//                        ApiKeys.SENDER_VERIFY_KEY,
//                        mobileNumber
//                    )
                    Log.d("sender", "setUpViewObservers: $response")
                    binding.apply {


                        val responseObject=response.data
                        try {
                            if (SenderMobileVerificationFragment.sType.equals("DMT3",true)
                                || SenderMobileVerificationFragment.sType.equals("UPI",true))
                            {
                                fragmentManager?.popBackStackImmediate()
                                verifySenderViewModel.verifySender(
                                    MainActivity.LOGIN_SESSION,
                                    ApiKeys.SENDER_VERIFY_KEY,
                                    mobileNumber
                                )
                            }
                            else
                            {

                                val dataArray=responseObject!!.getJSONArray("Data")
                                val dataObject=dataArray.getJSONObject(0)
                                state=dataObject.getString("state")

                                etFirstName.isClickable = false
                                etFirstName.isFocusableInTouchMode = false

                                etLastName.isClickable = false
                                etLastName.isFocusableInTouchMode = false

                                etPincode.isClickable = false
                                etPincode.isFocusableInTouchMode = false

                                etAddress.isClickable = false
                                etAddress.isFocusableInTouchMode = false

                                btnProceed.visibility = View.GONE

                                otpLy.visibility = View.VISIBLE
                            }


                        }
                        catch (e:Exception)
                        {
                            Toast.makeText(context,"Please try after sometime.",Toast.LENGTH_LONG).show()
                        }


                    }
                }
            }
        }

        verifySenderViewModel.verifySenderData.observe(viewLifecycleOwner)
        { response ->

            when (response) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    if (response.statusCode.equals("0")) {
                        val bundle = Bundle()
                        bundle.putString("number", mobileNumber)
                        replaceFragment(AddSenderFragment(), bundle)
                    }
                    Toast.makeText(context, response.errMessage, Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()

                }
            }
        }

        addSenderViewModel.verifySenderData.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    if (response.statusCode.equals("0")) {
                        val bundle = Bundle()
                        bundle.putString("number", mobileNumber)
                        replaceFragment(AddSenderFragment(), bundle)
                    }
                    Toast.makeText(context, response.errMessage, Toast.LENGTH_LONG).show()
                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    fragmentManager?.popBackStackImmediate()
                    verifySenderViewModel.verifySender(
                        MainActivity.LOGIN_SESSION,
                        ApiKeys.SENDER_VERIFY_KEY,
                        mobileNumber
                    )

                }
            }
        }
    }

    private fun setUpViewModels() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = AddSenderRepository(webService)
        val verifySenderRepository = SenderVerificationRepository(webService)
        addSenderViewModel = ViewModelProvider(
            this,
            AddSenderViewModelFactory(repository))[AddSenderViewModel::class.java]
        verifySenderViewModel = ViewModelProvider(
            this,
            SenderMobileVerificationViewModelFactory(verifySenderRepository)
        )[SenderMobileVerificationViewModel::class.java]
    }

    private fun handleClickAndEvents() {
        binding.apply {
            Glide.with(imgLogo).load(LoginActivity.logoImage).into(imgLogo)

            btnProceed.setOnClickListener {
                fName = etFirstName.text.toString().trim()
                lName = etLastName.text.toString().trim()
                pinCode = etPincode.text.toString().trim()
                address = etAddress.text.toString().trim()

                if (checkFormValidation()) {
                    addSenderViewModel.addSender(
                        MainActivity.LOGIN_SESSION,
                        ApiKeys.ADD_SENDER_KEY,
                        mobileNumber,
                        fName,
                        lName,
                        address,
                        pinCode
                    )
                }
            }

            btnVerify.setOnClickListener {
                otp = etOtp.text.toString().trim()
                if (otp.equals("", true)) {
                    etOtp.error = "Error"
                    etOtp.requestFocus()
                } else {
                    addSenderViewModel.verifySender(
                        MainActivity.LOGIN_SESSION,
                        ApiKeys.VALIDATE_SENDER_KEY,
                        mobileNumber,
                        otp,state
                    )
                }
            }
        }
    }

    private fun checkFormValidation(): Boolean {
        binding.apply {
            if (fName.equals("", true)) {
                etFirstName.requestFocus()
                etFirstName.error = "Required"
                return false
            } else if (lName.equals("", true)) {
                etLastName.requestFocus()
                etLastName.error = "Required"
                return false
            } else if (pinCode.length != 6) {
                etPincode.requestFocus()
                etPincode.error = "Invalid"
                return false
            } else if (address.equals("", true)) {
                etAddress.requestFocus()
                etAddress.error = "Required"
                return false
            }
            return true
        }
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_container, fragment)
        fragmentTransaction.addToBackStack("")
        fragmentTransaction.commit()
    }
}