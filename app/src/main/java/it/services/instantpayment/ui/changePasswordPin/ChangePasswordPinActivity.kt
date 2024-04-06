package it.services.instantpayment.ui.changePasswordPin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.databinding.ActivityChangePasswordPinBinding
import it.services.instantpayment.interfaces.ChangePasswordPinRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.changePassowordPin.ChangePasswordPinViewModel
import it.services.instantpayment.viewModels.changePassowordPin.ChangePasswordPinViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService


class ChangePasswordPinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordPinBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var sType: String
    private lateinit var viewModel: ChangePasswordPinViewModel
    private lateinit var progressDialog: AlertDialog
    private lateinit var phone:String
    private lateinit var email:String
    private lateinit var panCard:String
    private lateinit var otp:String
    private lateinit var newPasswordPin:String
    private lateinit var userId:String
    private var isForget=false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        activity = this

        progressDialog = CustomDialogs.getCustomProgressDialog(activity)
        sType = intent?.getStringExtra("sType").toString()
        isForget = intent?.getBooleanExtra("isForget",false) == true

        if (sType.equals("MPIN",true))
        {
            binding.etPassword.inputType = InputType.TYPE_CLASS_NUMBER
            binding.etPassword.filters = arrayOf(InputFilter.LengthFilter(4))

        }


        setUpViewModel()
        setUpObserver()




        binding.apply {
            etPassword.hint = "Enter new ${sType.lowercase()}"

            if (!isForget)
            {
                tvTitle.text = "Change ${sType.lowercase()}"
                tvSubTitle.text = "Fill below details to change ${sType.lowercase()}"

                phone=intent.getStringExtra("phone").toString()
                email=intent.getStringExtra("email").toString()
                panCard=intent.getStringExtra("panCard").toString()

                    etPhone.setText(phone)
                    etEmail.setText(email)
                    etPanCard.setText(panCard)

                    etPhone.visibility=View.GONE
                    etEmail.visibility=View.GONE
                    etPanCard.visibility=View.GONE

                    viewModel.sendOtp("${ApiKeys.BASE_URL}ForgotPassword",phone, email, panCard)


            }
            else
            {
                tvTitle.text = "Reset ${sType.lowercase()}"
                tvSubTitle.text = "Fill below details to reset ${sType.lowercase()}"

            }


            btnProceed.setOnClickListener {
                if (checkInputs()) {
                    phone=etPhone.text.toString().trim()
                    email=etEmail.text.toString().trim()
                    panCard=etPanCard.text.toString().trim()
                    if (etOtp.visibility!=View.VISIBLE)
                    {
                        viewModel.sendOtp("${ApiKeys.BASE_URL}ForgotPassword",phone, email, panCard)

                    }
                    else
                    {
                        val inputOtp=etOtp.text.toString().trim()

                        if (inputOtp.equals(otp,true))
                        {
                            newPasswordPin=etPassword.text.toString().trim()
                            if (sType.equals("Password",true))
                            {
                                viewModel.changePasswordPin("${ApiKeys.BASE_URL}ResetPasswordByOTP",userId,inputOtp,newPasswordPin)
                            }
                            else
                            {
                                viewModel.changePasswordPin("${ApiKeys.BASE_URL}ChangeTxnPin",userId,inputOtp,newPasswordPin)

                            }
                        }
                        else
                        {
                            etOtp.error="Invalid"
                        }
                    }
                }
            }
        }

    }

    private fun setUpObserver() {
        viewModel.changePasswordData.observe(this)
        {
            when (it) {
                is Response.Error -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(context)
                        .setMessage(it.errMessage)
                        .setPositiveButton("Ok", null)
                        .show()
                }

                is Response.Loading -> progressDialog.show()
                is Response.Success -> {
                    progressDialog.dismiss()
                    AlertDialog.Builder(context)
                        .setMessage(it.data)
                        .setPositiveButton(
                            "Ok"
                        ) { _, _ -> finish() }
                        .setCancelable(false)
                        .show()
                }
            }
        }

        viewModel.sendOtpData.observe(this)
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
                    val responseObject=it.data!!
                    userId=responseObject.getString("Message")
                    otp=responseObject.getString("Data")

                    binding.apply {
                        etPassword.visibility=View.VISIBLE
                        etOtp.visibility=View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setUpViewModel() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = ChangePasswordPinRepository(webService)
        viewModel = ViewModelProvider(
            this,
            ChangePasswordPinViewModelFactory(repository)
        )[ChangePasswordPinViewModel::class.java]
    }

    private fun checkInputs(): Boolean {
        binding.apply {

            if (TextUtils.isEmpty(etPhone.text.toString().trim())) {
                etPhone.error = "Required"
                return false
            }
            if (TextUtils.isEmpty(etEmail.text.toString().trim())) {
                etEmail.error = "Required"
                return false
            }
            if (TextUtils.isEmpty(etPanCard.text.toString().trim())) {
                etPanCard.error = "Required"
                return false
            }

            if (etPassword.visibility==View.VISIBLE)
            {
                if (TextUtils.isEmpty(etPassword.text.toString().trim())) {
                    etPassword.error = "Required"
                    return false
                }
            }
            return true
        }
    }
}