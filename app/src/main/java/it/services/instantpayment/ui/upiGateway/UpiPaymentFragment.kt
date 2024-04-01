package it.services.instantpayment.ui.upiGateway

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.databinding.FragmentUpiPaymentActivityBinding
import it.services.instantpayment.repository.Response
import it.services.instantpayment.repository.UpiGatewayRepository
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.utils.WebViewActivity
import it.services.instantpayment.viewModels.upiGateway.UpiGatewayViewModel
import it.services.instantpayment.viewModels.upiGateway.UpiGatewayViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService


class UpiPaymentFragment : Fragment() {

    private lateinit var binding: FragmentUpiPaymentActivityBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var amount: String
    private lateinit var mobileNo: String
    private lateinit var emailId: String
    private lateinit var customerName: String
    private lateinit var upiLink: String
    private lateinit var upiGatewayViewModel: UpiGatewayViewModel
    private lateinit var progressDialog: AlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpiPaymentActivityBinding.inflate(inflater, container, false)
        context = requireContext()
        activity = requireActivity()
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)

        setUpViewModels()
        setUpObservers()

        binding.apply {
            btnProceed.setOnClickListener {
                amount = etAmount.text.toString().trim()
                mobileNo = etNumber.text.toString().trim()
                emailId = etEmail.text.toString().trim()
                customerName = etCustomerName.text.toString().trim()

                upiGatewayViewModel.initiatePayment(
                    amount, mobileNo, emailId, customerName
                )

            }
        }

        return binding.root
    }

    private fun setUpObservers() {
        upiGatewayViewModel.paymentData.observe(viewLifecycleOwner)
        {
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
                    upiLink = it.data.toString()
                    startPayment()
                }

            }
        }
    }

    private fun startPayment() {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra("url", upiLink)
        startActivity(intent)
    }

    private fun setUpViewModels() {
        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = UpiGatewayRepository(webService)
        upiGatewayViewModel = ViewModelProvider(
            this,
            UpiGatewayViewModelFactory(repository)
        )[UpiGatewayViewModel::class.java]
    }
}