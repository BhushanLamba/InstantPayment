package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class BalanceRepository(private val webService: WebService) {
    private val balanceLiveData = MutableLiveData<Response<String>>()

    val balanceData: LiveData<Response<String>>
        get() = balanceLiveData

    suspend fun getBalance(sessionKey: String, apiKey: String) {
        balanceLiveData.postValue(Response.Loading())

        val result = webService.getBalance(sessionKey, apiKey)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    val dataArray = responseObject.getJSONArray("Data")

                    val dataObject = dataArray.getJSONObject(0)
                    val balance = dataObject.getString("CurrentBalance")
                    balanceLiveData.postValue(Response.Success(balance))
                } else {
                    val message = responseObject.getString("Message")
                    balanceLiveData.postValue(Response.Error(message, statusCode))
                }

            } else {
                balanceLiveData.postValue(Response.Error(result.message(), ""))
            }
        } catch (e: Exception) {
            balanceLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }
}