package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.MainActivity
import it.services.instantpayment.models.ReportsModel
import it.services.instantpayment.webService.WebService
import org.json.JSONArray
import org.json.JSONObject

class ReportsRepository(private val webService: WebService) {
    private val reportsListLiveData = MutableLiveData<Response<ArrayList<JSONObject>>>()

    private val receiptLiveData = MutableLiveData<Response<JSONObject>>()


    val reportsListData: LiveData<Response<ArrayList<JSONObject>>>
        get() = reportsListLiveData

    val receiptData: LiveData<Response<JSONObject>>
        get() = receiptLiveData

    suspend fun getReports(
        sessionKey: String,
        apiKey: String,
        serviceName: String,
        fromDate: String,
        toDate: String
    ) {
        reportsListLiveData.postValue(Response.Loading())

        val response =
            webService.getReports( sessionKey, apiKey, fromDate, toDate, serviceName,MainActivity.USER_TYPE)

        try {
            if (response.isSuccessful) {
                val responseObject = JSONObject(response.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {

                    val dataArrayStr = responseObject.getString("Data")
                    val dataArray = JSONArray(dataArrayStr)
                    val reportsList = ArrayList<JSONObject>()
                    for (position in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(position)
                        var date = dataObject.getString("ReqDate")
                        val amount = dataObject.getString("Amount")
                        val mobileNo = dataObject.getString("Mobileno")
                        val status = dataObject.getString("Status")
                        val operatorName = dataObject.getString("OperatorName")

                        date = date.split("T")[0]

                        val reportsModel =
                            ReportsModel(date, amount, mobileNo, operatorName, status)

                        //reportsList.add(reportsModel)
                        reportsList.add(dataObject)
                    }
                    reportsListLiveData.postValue(Response.Success(reportsList))
                } else {
                    val message = responseObject.getString("Message")
                    reportsListLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                reportsListLiveData.postValue(Response.Error(response.message(), ""))
            }
        } catch (e: Exception) {
            reportsListLiveData.postValue(e.localizedMessage?.let { Response.Error(it, "") })
        }

    }

    suspend fun getReceipt(sessionKey: String, apiKey: String, txnId: String, serviceName: String) {
        receiptLiveData.postValue(Response.Loading())

        val response = webService.getReceipt("TxnReceipts", sessionKey, apiKey, txnId, serviceName)

        try {
            if (response.isSuccessful) {
                val responseObject = JSONObject(response.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {

                    val dataArray = responseObject.getJSONArray("Data")
                    val dataObject = dataArray.getJSONObject(0)
                    receiptLiveData.postValue(Response.Success(dataObject))
                } else {
                    val message = responseObject.getString("Message")
                    receiptLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                receiptLiveData.postValue(Response.Error(response.message(), ""))
            }
        } catch (e: Exception) {
            receiptLiveData.postValue(e.localizedMessage?.let { Response.Error(it, "") })
        }
    }
}