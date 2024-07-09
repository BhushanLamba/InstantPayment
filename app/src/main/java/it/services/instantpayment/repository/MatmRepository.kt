package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import it.services.instantpayment.MainActivity
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class MatmRepository(private val webService: WebService) {
    private val mAtmInitiateLiveData=MutableLiveData<Response<String>>()

    val mAtmInitiateData:LiveData<Response<String>>
        get() = mAtmInitiateLiveData

    suspend fun initiateMAtmTransaction(amount:String,service:String)
    {
        mAtmInitiateLiveData.postValue(Response.Loading())

        val response=webService.initiateMatm(MainActivity.LOGIN_SESSION,ApiKeys.INITIATE_M_ATM_KEY,amount,service)

        try {
            if (response.body()!=null)
            {
                val responseObject=JSONObject(response.body().toString())
                val statusCode=responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    val clientTxnId = responseObject.getString("Data")

                    mAtmInitiateLiveData.postValue(Response.Success(clientTxnId))
                } else {
                    val message = responseObject.getString("Message")
                    mAtmInitiateLiveData.postValue(Response.Error(message, statusCode))
                }
            }
            else
            {
                mAtmInitiateLiveData.postValue(Response.Error(response.message(),""))
            }
        }
        catch (e:Exception)
        {
            mAtmInitiateLiveData.postValue(e.localizedMessage?.let { Response.Error(it,"") })
        }


    }


}