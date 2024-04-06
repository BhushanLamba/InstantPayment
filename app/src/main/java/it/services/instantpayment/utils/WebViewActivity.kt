package it.services.instantpayment.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.MainActivity
import it.services.instantpayment.databinding.ActivityWebViewBinding
import it.services.instantpayment.repository.PaymentRepository
import it.services.instantpayment.repository.Response
import it.services.instantpayment.viewModels.razorPay.PaymentViewModel
import it.services.instantpayment.viewModels.razorPay.PaymentViewModelFactory
import it.services.instantpayment.webService.RetrofitClient
import it.services.instantpayment.webService.WebService
import org.json.JSONArray
import org.json.JSONObject

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWebViewBinding
    private lateinit var url:String

    private lateinit var progressDialog: AlertDialog



    companion object
    {
        private val liveData=MutableLiveData<Response<String>>()
        val data:LiveData<Response<String>>
            get() = liveData
        lateinit var paymentViewModel: PaymentViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url=intent.getStringExtra("url").toString()
        progressDialog = CustomDialogs.getCustomProgressDialog(this)

        setUpViewModels()
        setUpObservers()
        initWebView()

    }

    private fun setUpViewModels() {

        val webService = RetrofitClient.getInstance().create(WebService::class.java)
        val repository = PaymentRepository(webService)
        paymentViewModel = ViewModelProvider(
            this,
            PaymentViewModelFactory(repository)
        )[PaymentViewModel::class.java]
    }

    private fun setUpObservers() {

        paymentViewModel.updatePaymentData.observe(this)
        {
            when(it)
            {
                is Response.Error ->
                {
                    progressDialog.dismiss()
                    finish()
                }
                is Response.Loading -> {
                    progressDialog.show()
                }
                is Response.Success ->
                {
                    liveData.postValue(it)
                    progressDialog.dismiss()
                    finish()
                }

            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.apply {

            webview.loadUrl(url)
            webview.settings.javaScriptEnabled = true
            webview.settings.loadWithOverviewMode = true
            webview.settings.setSupportMultipleWindows(true)
            // Do not change Useragent otherwise it will not work. even if not working uncommit below
            // mWebView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.135 Mobile Safari/537.36");
            webview.webChromeClient = WebChromeClient()
            webview.addJavascriptInterface(
                WebViewInterface(),
                "Interface"
            )
        }

    }

    class WebViewInterface {
        @JavascriptInterface
        fun paymentResponse(client_txn_id: String, txn_id: String) {
            paymentViewModel.updatePayment(
                "UpdateUPIPaymentStatus",
                MainActivity.LOGIN_SESSION,
                ApiKeys.UPDATE_UPI_PAYMENT_KEY,
                txn_id,
                client_txn_id,
                ""
            )
        }

        @JavascriptInterface
        fun errorResponse() {
            Log.d("upiResponse", "errorResponse: Some error has occurred")
        }
    }

}