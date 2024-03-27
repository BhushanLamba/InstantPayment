package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.webService.WebService
import org.json.JSONObject
import java.lang.Exception

class BbpsBillFetchRepository(private val webService: WebService) {
    private val billFetchLiveData=MutableLiveData<Response<JSONObject>>()
    val billFetchData:LiveData<Response<JSONObject>>
        get() = billFetchLiveData

    suspend fun fetchBill(sessionKey:String,apiKey:String,opId:String,accountNo:String)
    {
        billFetchLiveData.postValue(Response.Loading())
        val result=webService.fetchBill(sessionKey,apiKey,opId,accountNo)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {
                    billFetchLiveData.postValue(Response.Success(responseObject))
                } else {
                    val message = responseObject.getString("Message")
                    billFetchLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                billFetchLiveData.postValue(Response.Error(result.message(), ""))
            }
        }
        catch (e:Exception)
        {
            billFetchLiveData.postValue(Response.Error(e.localizedMessage,""))
        }

    }
}