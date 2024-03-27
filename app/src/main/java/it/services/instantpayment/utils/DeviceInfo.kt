package it.services.instantpayment.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import java.lang.Exception

object DeviceInfo {

    fun getDeviceManufactureModel(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return manufacturer + model
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context) :String
    {
        var deviceId:String=""
        try {
            deviceId=Settings.Secure.getString(context.contentResolver,Settings.Secure.ANDROID_ID)
        }
        catch (ignore:Exception){}
        return deviceId
    }


}