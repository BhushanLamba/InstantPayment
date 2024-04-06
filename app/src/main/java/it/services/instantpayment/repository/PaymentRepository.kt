package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class PaymentRepository(val webService: WebService) {
    private val paymentLiveData=MutableLiveData<Response<String>>()

    private val updatePaymentLiveData=MutableLiveData<Response<String>>()

    val paymentData:LiveData<Response<String>>
        get() = paymentLiveData

    val updatePaymentData:LiveData<Response<String>>
        get() = updatePaymentLiveData


    suspend fun initiatePayment(sessionKey: String, apiKey: String,amount:String,mobileNo:String,panCard:String,
    aadhaarNo:String) {
        paymentLiveData.postValue(Response.Loading())

        val result = webService.initiatePayment(ApiKeys.BASE_URL+"InitiatePayment",sessionKey, apiKey,amount,mobileNo,panCard,aadhaarNo)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    val orderId = responseObject.getString("Data")
                    paymentLiveData.postValue(Response.Success(orderId))


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

    suspend fun updatePayment(endPoint:String,sessionKey: String, apiKey: String,orderId:String,response:String,status:String) {
        updatePaymentLiveData.postValue(Response.Loading())

        val result = webService.updatePayment(ApiKeys.BASE_URL+endPoint,sessionKey, apiKey,orderId,response,status)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("status")
                if (statusCode.equals("1")) {
                    updatePaymentLiveData.postValue(Response.Success(orderId))


                } else {
                    val message = responseObject.getString("Message")
                    updatePaymentLiveData.postValue(Response.Error(message, statusCode))
                }

            } else {
                updatePaymentLiveData.postValue(Response.Error(result.message(), ""))
            }
        } catch (e: Exception) {
            updatePaymentLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }

}