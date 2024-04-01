package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class UpiGatewayRepository(private val webService: WebService)
{
    private val paymentLiveData= MutableLiveData<Response<String>>()

    val paymentData:LiveData<Response<String>>
        get() = paymentLiveData

    suspend fun initiatePayment(sessionKey: String, apiKey: String,amount:String,mobileNo:String,emailId:String,
                                customerName:String) {
        paymentLiveData.postValue(Response.Loading())

        val result = webService.initiateUpiPayment(ApiKeys.BASE_URL+"InitiateUPIPayment",sessionKey, apiKey,amount,mobileNo,emailId,customerName)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    val link = responseObject.getString("Data")
                    paymentLiveData.postValue(Response.Success(link))


                } else {
                    val message = responseObject.getString("Message")
                    paymentLiveData.postValue(Response.Error(message, statusCode))
                }

            } else {
                paymentLiveData.postValue(Response.Error(result.message(), ""))
            }
        } catch (e: Exception) {
            paymentLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }

}