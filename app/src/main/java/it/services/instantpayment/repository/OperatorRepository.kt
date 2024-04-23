package it.services.instantpayment.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.models.OperatorModel
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class OperatorRepository(private val webService: WebService) {
    private val operatorLiveData = MutableLiveData<Response<ArrayList<OperatorModel>>>()
    val operatorData: LiveData<Response<ArrayList<OperatorModel>>>
        get() = operatorLiveData

    private val bbpsOperatorLiveData=MutableLiveData<Response<ArrayList<OperatorModel>>>()
    val bbpsOperatorData:LiveData<Response<ArrayList<OperatorModel>>>
        get() = bbpsOperatorLiveData



    suspend fun getOperator(sessionKey: String, apiKey: String, serviceId: String) {
        operatorLiveData.postValue(Response.Loading())

        val result = webService.getOperator(sessionKey, apiKey, serviceId)

        try {
            if (result.body() != null) {
                Log.d("response", "operatorData: " + result.body())
                val responseObject = JSONObject(result.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {
                    val dataArray=responseObject.getJSONArray("Data")
                    val operatorList=ArrayList<OperatorModel>()


                    for (position in 0 until dataArray.length())
                    {
                        val dataObject=dataArray.getJSONObject(position)

                        val id=dataObject.getString("Id")
                        val serviceId=dataObject.getString("ServiceId")
                        val operatorName=dataObject.getString("OperatorName")
                        val operatorImage=dataObject.getString("Picture")

                        val operatorModel=OperatorModel(id, serviceId, operatorName,operatorImage)

                        operatorList.add(operatorModel)
                    }
                    operatorLiveData.postValue(Response.Success(operatorList))



                } else {
                    val message = responseObject.getString("Message")
                    operatorLiveData.postValue(Response.Error(message, statusCode))
                }
            }
            else {
                operatorLiveData.postValue(Response.Error(result.message(), ""))
            }
        } catch (e: Exception) {
            operatorLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }

    suspend fun getBbpsOperator(sessionKey: String, apiKey: String, serviceId: String)
    {
        bbpsOperatorLiveData.postValue(Response.Loading())
        val result=webService.getBbpsOperator(sessionKey,apiKey,serviceId)

        try {
            if (result.body() != null) {
                Log.d("response", "operatorData: " + result.body())
                val responseObject = JSONObject(result.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {
                    val dataArray=responseObject.getJSONArray("Data")
                    val operatorList=ArrayList<OperatorModel>()


                    for (position in 0 until dataArray.length())
                    {
                        val dataObject=dataArray.getJSONObject(position)

                        val id=dataObject.getString("Id")
                        val operatorName=dataObject.getString("OperatorName")
                        val operatorImage=dataObject.getString("Picture")

                        val operatorModel=OperatorModel(id, serviceId, operatorName, operatorImage)

                        operatorList.add(operatorModel)
                    }
                    bbpsOperatorLiveData.postValue(Response.Success(operatorList))



                } else {
                    val message = responseObject.getString("Message")
                    bbpsOperatorLiveData.postValue(Response.Error(message, statusCode))
                }
            }
            else {
                bbpsOperatorLiveData.postValue(Response.Error(result.message(), ""))
            }
        }
        catch (e:Exception)
        {
            bbpsOperatorLiveData.postValue(Response.Error(e.localizedMessage, ""))

        }
    }

}