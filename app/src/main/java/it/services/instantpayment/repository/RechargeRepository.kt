package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class RechargeRepository(private val webService: WebService) {
    private val rechargeLiveData = MutableLiveData<Response<JSONObject>>()

    val rechargeData: LiveData<Response<JSONObject>>
        get() = rechargeLiveData

    suspend fun doRecharge(
        sessionKey: String,
        apiKey: String,
        serviceId: String,
        operatorId: String,
        account: String,
        amount: String,
        mpin: String
    ) {

        rechargeLiveData.postValue(Response.Loading())

        val result =
            webService.doRecharge(sessionKey, apiKey, serviceId, operatorId, account, amount,mpin)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {
                    rechargeLiveData.postValue(Response.Success(responseObject))
                } else {
                    val message = responseObject.getString("Message")
                    rechargeLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                rechargeLiveData.postValue(Response.Error(result.message(), ""))
            }
        } catch (e: Exception) {
            rechargeLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }

    }

}