package it.services.instantpayment.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {

    private const val INSTANT_PAYMENT = "instantPayment"

    const val LOGIN_DATA_KEY="loginData"
    const val USER_NAME="userName"
    const val PASSWORD="password"
    const val DEVICE_NAME="deviceName"
    const val DEVICE_PACKAGE="devicePackage"

    fun setString(context: Context, key: String, value: String) {
        val editor = context.getSharedPreferences(INSTANT_PAYMENT, MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String): String? {
        val sharedPreference = context.getSharedPreferences(INSTANT_PAYMENT, MODE_PRIVATE)
        return sharedPreference.getString(key, "")
    }

    fun setBoolean(context: Context, key: String, value: Boolean) {
        val editor = context.getSharedPreferences(INSTANT_PAYMENT, MODE_PRIVATE).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(context: Context, key: String): Boolean {
        val sharedPreference = context.getSharedPreferences(INSTANT_PAYMENT, MODE_PRIVATE)

        return sharedPreference.getBoolean(key, false)
    }

}