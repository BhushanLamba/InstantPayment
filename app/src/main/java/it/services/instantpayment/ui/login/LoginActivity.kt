package it.services.instantpayment.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.MainActivity
import it.services.instantpayment.databinding.ActivityLoginBinding
import it.services.instantpayment.repository.LoginRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.utils.DeviceInfo
import it.services.instantpayment.utils.LocationUtils
import it.services.instantpayment.utils.LocationViewModelFactory
import it.services.instantpayment.utils.SharedPref
import it.services.instantpayment.utils.SharedPref.PASSWORD
import it.services.instantpayment.utils.SharedPref.USER_NAME
import it.services.instantpayment.viewModels.login.LoginViewModel
import it.services.instantpayment.viewModels.login.LoginViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var context: Context
    private lateinit var activity: Activity
    lateinit var userName: String
    lateinit var password: String
    private var latitude: String = ""
    private var longitude: String = ""

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var locationUtils: LocationUtils

    private lateinit var progressDialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        activity = this
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)

        setUpViewModel()

        handleClicksAndEvents()

    }

    private fun setUpViewModel() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = LoginRepository(webService)

        loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(
                repository
            )
        )[LoginViewModel::class.java]

        loginViewModel.loginData.observe(
            this
        ) {
            when (it) {
                is Response.Loading -> {
                    progressDialog.show()
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    Toast.makeText(context, it.errMessage, Toast.LENGTH_LONG).show()

                }

                is Response.Success -> {
                    progressDialog.dismiss()
                    it.data?.let {
                        if (binding.ckbRemember.isChecked) {
                            SharedPref.setString(context, USER_NAME, userName)
                            SharedPref.setString(context, PASSWORD, password)
                        }
                        startActivity(Intent(context, MainActivity::class.java))
                        finish()
                    }
                }

            }
        }


        locationUtils =
            ViewModelProvider(this, LocationViewModelFactory())[LocationUtils::class.java]
        locationUtils.getLocation(context, activity)

        locationUtils.latitude.observe(this) {
            latitude = it

        }
        locationUtils.longitude.observe(this) {
            longitude = it
        }


    }

    private fun handleClicksAndEvents() {


        userName = SharedPref.getString(context, USER_NAME).toString()
        password = SharedPref.getString(context, PASSWORD).toString()

        if (!(userName.equals("", true) && password.equals("", true))) {
            binding.apply {
                etUserId.setText(userName)
                etPassword.setText(password)
            }
        }


        binding.apply {

            btnLogin.setOnClickListener {
                userName = etUserId.text.toString().trim()
                password = etPassword.text.toString().trim()
                if (userName.equals("", true)) {
                    etUserId.error = "Required"
                    etUserId.requestFocus()
                } else if (password.equals("", true)) {
                    etPassword.error = "Required"
                    etPassword.requestFocus()
                } else if (latitude.equals("", true) && longitude.equals("", true)) {
                    Toast.makeText(
                        context,
                        "Location not found . Please restart application",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    loginViewModel.loginUser(
                        userName,
                        password,
                        DeviceInfo.getDeviceId(context),
                        "",
                        "",
                        latitude,
                        longitude,
                        context
                    )

                }
            }
        }
    }
}



