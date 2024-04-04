package it.services.instantpayment.ui.changePasswordPin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.databinding.FragmentChangePasswordPinBinding
import it.services.instantpayment.interfaces.ChangePasswordPinRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.changePassowordPin.ChangePasswordPinViewModel
import it.services.instantpayment.viewModels.changePassowordPin.ChangePasswordPinViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService


class ChangePasswordPinFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordPinBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var sType: String
    private lateinit var viewModel: ChangePasswordPinViewModel
    private lateinit var progressDialog: AlertDialog

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordPinBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()

        progressDialog = CustomDialogs.getCustomProgressDialog(activity)
        sType = arguments?.getString("sType").toString()

        setUpViewModel()
        setUpObserver()

        binding.apply {
            tvTitle.text = "Change ${sType.lowercase()}"
            tvSubTitle.text = "Fill below details to change ${sType.lowercase()}"
            etOldPasswordPin.setText("Enter old ${sType.lowercase()}")
            etNewPasswordPin.setText("Enter new ${sType.lowercase()}")
            etConfirmPasswordPin.setText("Confirm new ${sType.lowercase()}")


            btnProceed.setOnClickListener {
                if (checkInputs()) {
                    viewModel.changePasswordPin()
                }
            }
        }

        return binding.root
    }

    private fun setUpObserver() {
        viewModel.changePasswordData.observe(viewLifecycleOwner)
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
                        .setMessage(it.errMessage)
                        .setPositiveButton(
                            "Ok"
                        ) { _, _ -> getActivity()?.supportFragmentManager?.popBackStackImmediate() }
                        .setCancelable(false)
                        .show()
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

            if (TextUtils.isEmpty(etOldPasswordPin.text.toString().trim())) {
                etOldPasswordPin.error = "Required"
                return false
            }
            if (TextUtils.isEmpty(etNewPasswordPin.text.toString().trim())) {
                etNewPasswordPin.error = "Required"
                return false
            }
            if (etConfirmPasswordPin.text.toString().trim()
                    .equals(etNewPasswordPin.text.toString().trim(), false)
            ) {
                Toast.makeText(
                    context,
                    "New Password and Confirm Password must be same",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            return true
        }
    }
}