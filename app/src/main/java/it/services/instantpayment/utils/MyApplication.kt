package it.services.instantpayment.utils

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.finopaytech.finosdk.helpers.FinoApplication

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FinoApplication.init(this);
    }

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
        MultiDex.install(context)
    }
}