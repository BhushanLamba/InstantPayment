package it.services.instantpayment.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.Result
import it.services.instantpayment.R
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerViewActivity : AppCompatActivity() , ZXingScannerView.ResultHandler {

    private lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView=ZXingScannerView(this)
        setContentView(scannerView)

        scannerView.startCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Log.i("qrcode", rawResult.toString())
        val data=rawResult.toString()
        val intent= Intent()
        intent.putExtra("qrCodeData",data)
        setResult(1,intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }
}