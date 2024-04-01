package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.webService.WebService
import org.json.JSONObject
import java.lang.Exception

class BbpsBillPayRepository(val webService: WebService) {
   private val bbpsBillPayLiveData=MutableLiveData<Response<JSONObject>>()

    val bbpsBillPayData:LiveData<Response<JSONObject>>
        get() = bbpsBillPayLiveData

    suspend fun payBill(sessionKey:String,apiKey:String,serviceId:String,operatorId:String,mobileNo:String,accountNumber:String,amount:String,
                        provider:String,dueDate:String,mpin:String,apiResponse:String)
    {
        bbpsBillPayLiveData.postValue(Response.Loading())

        val result=webService.payBill(sessionKey,apiKey,serviceId,operatorId,mobileNo,accountNumber,amount,provider,dueDate,mpin,apiResponse)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {
                    bbpsBillPayLiveData.postValue(Response.Success(responseObject))
                } else {
                    val message = responseObject.getString("Message")
                    bbpsBillPayLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                bbpsBillPayLiveData.postValue(Response.Error(result.message(), ""))
            }
        }
        catch (e:Exception)
        {
            bbpsBillPayLiveData.postValue(Response.Error(e.localizedMessage,""))
        }
    }
}