package it.services.instantpayment.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.repository.Response
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONObject
import java.lang.Exception

class ChangePasswordPinRepository(private val webService: WebService) {
    private val changePasswordLiveData=MutableLiveData<Response<String>>()
    val changePasswordData:LiveData<Response<String>>
        get() = changePasswordLiveData

    private val sendOtpLiveData=MutableLiveData<Response<JSONObject>>()
    val sendOtpData:LiveData<Response<JSONObject>>
        get() = sendOtpLiveData

    suspend fun sendOtp(url:String,phone:String,email:String,panCard:String)
    {
        val response=webService.sendOtpPasswordPin(url,phone,email,panCard)
        sendOtpLiveData.postValue(Response.Loading())

        try {
            if (response.isSuccessful)
            {
                val responseObject=JSONObject(response.body().toString())

                val statusCode=responseObject.getString("Status_Code")
                val message=responseObject.getString("Message")

                if (statusCode.equals("1",true))
                {
                    sendOtpLiveData.postValue(Response.Success(responseObject))
                }
                else
                {
                    sendOtpLiveData.postValue(Response.Error(message,""))
                }
            }
            else
            {
                sendOtpLiveData.postValue(Response.Error("Try after sometime",""))
            }
        }
        catch (e:Exception)
        {
            sendOtpLiveData.postValue(Response.Error("Try after sometime",""))
        }
    }

    suspend fun changePasswordPin(url: String, userId: String, inputOtp: String, newPasswordPin: String) {
        val response=webService.changePasswordPin(url,userId,inputOtp,newPasswordPin,newPasswordPin)
        changePasswordLiveData.postValue(Response.Loading())

        try {
            if (response.isSuccessful)
            {
                val responseObject=JSONObject(response.body().toString())

                val statusCode=responseObject.getString("Status_Code")
                val message=responseObject.getString("Message")

                if (statusCode.equals("1",true))
                {
                    changePasswordLiveData.postValue(Response.Success(message))
                }
                else
                {
                    changePasswordLiveData.postValue(Response.Error(message,""))
                }
            }
            else
            {
                changePasswordLiveData.postValue(Response.Error("Try after sometime",""))
            }
        }
        catch (e:Exception)
        {
            changePasswordLiveData.postValue(Response.Error("Try after sometime",""))
        }
    }
}