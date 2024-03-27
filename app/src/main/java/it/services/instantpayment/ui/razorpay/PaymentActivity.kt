package it.services.instantpayment.ui.razorpay

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import it.services.instantpayment.MainActivity
import it.services.instantpayment.databinding.ActivityPaymentBinding
import it.services.instantpayment.repository.PaymentRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.ApiKeys.INITIATE_PAYMENT_KEY
import it.services.instantpayment.utils.CustomDialogs
import it.services.instantpayment.viewModels.razorPay.PaymentViewModel
import it.services.instantpayment.viewModels.razorPay.PaymentViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener, ExternalWalletListener {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var amount: String
    private lateinit var mobileNumber: String
    private lateinit var panNumberNumber: String
    private lateinit var aadhaarNumber: String
    private lateinit var orderId: String
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        activity = this
        progressDialog = CustomDialogs.getCustomProgressDialog(activity)

        setUpViewModels()
        setUpObservers()

        binding.apply {
            btnProceed.setOnClickListener {
                amount = etAmount.text.toString().trim()
                mobileNumber = etNumber.text.toString().trim()
                panNumberNumber = etPan.text.toString().trim()
                aadhaarNumber = etAadhaar.text.toString().trim()

                paymentViewModel.initiatePayment(
                    MainActivity.LOGIN_SESSION, INITIATE_PAYMENT_KEY, amount,
                    mobileNumber, panNumberNumber, aadhaarNumber
                )

            }
        }

    }

    private fun setUpObservers() {
        paymentViewModel.paymentData.observe(this)
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
                    orderId = it.data.toString()
                    startPayment()
                }

            }
        }

        paymentViewModel.updatePaymentData.observe(this)
        {
            finish()
        }
    }

    private fun setUpViewModels() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = PaymentRepository(webService)
        paymentViewModel = ViewModelProvider(
            this,
            PaymentViewModelFactory(repository)
        )[PaymentViewModel::class.java]
    }

    private fun startPayment() {

        var amountInt = amount.toInt()
        amountInt *= 100

        amount = amountInt.toString()

        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name", "Instant Payment")
            options.put("description", "Wallet TopUp")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#1A5B91")
            options.put("currency", "INR")
            //options.put("order_id", "1841854819")
            options.put("amount", amount)//pass amount in currency subunits

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", "krishany365@gmail.com")
            prefill.put("contact", mobileNumber)

            options.put("prefill", prefill)
            co.setKeyID("rzp_live_ccf8cDWgjWkpM6")
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(response: String, p1: PaymentData?) {
        Toast.makeText(activity, response, Toast.LENGTH_LONG).show()
        paymentViewModel.updatePayment(
            MainActivity.LOGIN_SESSION,
            ApiKeys.UPDATE_PAYMENT_KEY,
            orderId,
            response,
            "SUCCESS"
        )
    }

    override fun onPaymentError(p0: Int, response: String, p2: PaymentData?) {
        Toast.makeText(activity, response, Toast.LENGTH_LONG).show()
        paymentViewModel.updatePayment(
            MainActivity.LOGIN_SESSION,
            ApiKeys.UPDATE_PAYMENT_KEY,
            orderId,
            response,
            "FAILED"
        )

    }

    override fun onExternalWalletSelected(response: String, p1: PaymentData?) {
        Toast.makeText(activity, response, Toast.LENGTH_LONG).show()
        paymentViewModel.updatePayment(
            MainActivity.LOGIN_SESSION,
            ApiKeys.UPDATE_PAYMENT_KEY,
            orderId,
            response,
            "FAILED"
        )

    }
}