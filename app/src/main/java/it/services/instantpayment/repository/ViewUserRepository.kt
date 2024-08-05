package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.webService.WebService
import org.json.JSONArray
import org.json.JSONObject

class ViewUserRepository(private val webService: WebService) {
    private val viewUserLiveData = MutableLiveData<Response<JSONArray>>()
    private val creditDebitLiveData = MutableLiveData<Response<String>>()
    val viewUserData: LiveData<Response<JSONArray>>
        get() = viewUserLiveData


    val creditDebitData: LiveData<Response<String>>
        get() = creditDebitLiveData


    suspend fun viewUser(sessionKey: String, apiKey: String, userType: String) {
        viewUserLiveData.postValue(Response.Loading())
        val result = webService.viewUser(sessionKey, apiKey, userType)

        try {

            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                val message = responseObject.getString("Message")

                if (statusCode.equals("1", true)) {
                    val data = responseObject.getJSONArray("Data")
                    viewUserLiveData.postValue(Response.Success(data))
                } else {
                    viewUserLiveData.postValue(Response.Error(message, ""))

                }
            } else {
                viewUserLiveData.postValue(Response.Error(result.message(), ""))
            }

        } catch (e: Exception) {
            viewUserLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }

    }

    suspend fun creditDebitUser(sessionKey: String, apiKey: String,txnType:String,transferUserId:String,
                                amount:String,mpin:String)
    {
        creditDebitLiveData.postValue(Response.Loading())

        val result = webService.creditDebitUser(sessionKey,apiKey,txnType,transferUserId,amount,mpin)

        try {

            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                val message = responseObject.getString("Message")

                if (statusCode.equals("1", true)) {
                    creditDebitLiveData.postValue(Response.Success(message))
                } else {
                    creditDebitLiveData.postValue(Response.Error(message, ""))

                }
            } else {
                creditDebitLiveData.postValue(Response.Error(result.message(), ""))
            }

        } catch (e: Exception) {
            creditDebitLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }

}