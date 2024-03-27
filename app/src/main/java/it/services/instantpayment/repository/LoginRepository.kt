package it.services.instantpayment.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.utils.ApiKeys.LOGIN_KEY
import it.services.instantpayment.utils.SharedPref
import it.services.instantpayment.utils.SharedPref.LOGIN_DATA_KEY
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class LoginRepository(private val webService: WebService) {

    private val loginLiveData = MutableLiveData<Response<JSONObject>>()

    val loginData: LiveData<Response<JSONObject>>
        get() = loginLiveData

    suspend fun loginUser(
        userName: String,
        password: String,
        simNumber: String,
        token: String,
        deviceInfo: String,
        lat: String,
        lng: String,
        context: Context
    ) {
        loginLiveData.postValue(Response.Loading())

        val result = webService.loginUser(
            userName,
            password,
            lat,
            lng,
            LOGIN_KEY,
            simNumber,
            token,
            deviceInfo
        )

        try {
            if (result.body() != null) {
                Log.d("response", "loginUser: " + result.body())
                val responseObject = JSONObject(result.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {
                    SharedPref.setString(context, LOGIN_DATA_KEY, responseObject.toString())
                    loginLiveData.postValue(Response.Success(responseObject))
                } else {
                    val message = responseObject.getString("Message")
                    loginLiveData.postValue(Response.Error(message, statusCode))
                }

            } else {
                loginLiveData.postValue(Response.Error(result.message(), ""))
            }
        } catch (e: Exception) {
            loginLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }

    }
}