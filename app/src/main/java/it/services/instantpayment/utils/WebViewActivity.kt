package it.services.instantpayment.utils

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import it.services.instantpayment.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWebViewBinding
    private lateinit var url:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        url=intent.getStringExtra("url").toString()

        initWebView()

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

        }

        @JavascriptInterface
        fun errorResponse() {

        }
    }

}