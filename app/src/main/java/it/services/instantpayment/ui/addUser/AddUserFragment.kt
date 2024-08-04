package it.services.instantpayment.ui.addUser

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.MainActivity
import it.services.instantpayment.databinding.FragmentAddUserBinding
import it.services.instantpayment.repository.AddUserRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.addUser.AddUserVideoModelFactory
import it.services.instantpayment.viewModels.addUser.AddUserViewModel
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService


class AddUserFragment : Fragment() {

    private lateinit var binding: FragmentAddUserBinding
    private lateinit var addUserViewModel: AddUserViewModel
    private lateinit var progressDialog:AlertDialog
    private lateinit var activity:Activity
    private lateinit var context:Context
    private lateinit var newUserType:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddUserBinding.inflate(inflater, container, false)
        activity=requireActivity()
        context=requireContext()
        progressDialog=CustomDialogs.getCustomProgressDialog(activity)
        newUserType=arguments?.getString("newUserType").toString()

        setUpViewModel()
        setUpObserver()

        binding.apply {

            btnNextBusinessDetails.setOnClickListener {
                val requiredFields = arrayOf(etShopName,etShopAddress, etShopState, etShopCity, etShopPinCode)

                if (validateInputs(requiredFields)) {
                    businessDetailsLy.visibility = View.GONE
                    personalDetailsLy.visibility = View.VISIBLE
                }
            }

            btnNextPersonalDetails.setOnClickListener {
                val requiredFields = arrayOf(
                    etLoginUserName,
                    etCustomerName,
                    etEmailId,
                    etMobile,
                    etPassword,
                    etAddressLine1,
                    etAddressLine2,
                    etState,
                    etCity,
                    etPincode
                )

                if (validateInputs(requiredFields)) {
                    personalDetailsLy.visibility = View.GONE
                    kycDetailsLy.visibility = View.VISIBLE
                }
            }

            btnFinish.setOnClickListener {
                val requiredFields = arrayOf(etAadhaar, etPan)

                if (validateInputs(requiredFields)) {
                    binding.apply {
                        val shopName=etShopName.text.toString().trim()
                        val shopAddress=etShopAddress.text.toString().trim()
                        val shopState=etShopState.text.toString().trim()
                        val shopCity=etShopCity.text.toString().trim()
                        val shopPinCode=etShopPinCode.text.toString().trim()
                        val loginUsername=etLoginUserName.text.toString().trim()
                        val customerName=etCustomerName.text.toString().trim()
                        val emailId=etEmailId.text.toString().trim()
                        val mobileNo=etMobile.text.toString().trim()
                        val password=etPassword.text.toString().trim()
                        val addressLineOne=etAddressLine1.text.toString().trim()
                        val addressLineTwo=etAddressLine2.text.toString().trim()
                        val state=etState.text.toString().trim()
                        val city=etCity.text.toString().trim()
                        val pinCode=etPincode.text.toString().trim()
                        val panNumber=etPan.text.toString().trim()
                        val aadhaarNumber=etAadhaar.text.toString().trim()


                        addUserViewModel.addUser(MainActivity.LOGIN_SESSION,
                            "AddUsers001",loginUsername,shopAddress,shopState,shopCity,shopPinCode,
                            shopName,"NA",customerName,emailId,mobileNo,password,panNumber,aadhaarNumber,
                            addressLineOne,addressLineTwo,state,city,pinCode,"NA","NA","NA",
                            MainActivity.USER_TYPE,newUserType
                        )
                    }

                }
            }
        }

        return binding.root
    }

    private fun setUpObserver() {
        addUserViewModel.addUserData.observe(viewLifecycleOwner)
        {
            when(it)
            {
                is Response.Error -> {
                    progressDialog.dismiss()
                    CustomDialogs.getMessageDialog(activity,it.errMessage.toString(),false)
                }
                is Response.Loading -> {
                    progressDialog.show()
                }
                is Response.Success -> {
                    progressDialog.dismiss()
                    CustomDialogs.getMessageDialog(activity,it.data.toString(),false)

                }
            }
        }
    }

    private fun setUpViewModel() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = AddUserRepository(webService)

        addUserViewModel = ViewModelProvider(
            this,
            AddUserVideoModelFactory(repository)
        )[AddUserViewModel::class.java]
    }

    private fun validateInputs(requiredFields: Array<EditText>): Boolean {
        for (field in requiredFields) {
            if (TextUtils.isEmpty(field.getText().toString())) {
                field.error = "Required"
                field.requestFocus()
                return false
            }
            if (field == binding.etMobile) {
                if (binding.etMobile.text.toString().length != 10) {
                    field.error = "Invalid"
                    field.requestFocus()
                    return false
                }
            }
            if (field == binding.etPan) {
                if (binding.etPan.text.toString().length != 10) {
                    field.error = "Invalid"
                    field.requestFocus()
                    return false
                }
            }

            if (field == binding.etAadhaar) {
                if (binding.etAadhaar.text.toString().length != 12) {
                    field.error = "Invalid"
                    field.requestFocus()
                    return false
                }
            }
        }
        return true
    }
}