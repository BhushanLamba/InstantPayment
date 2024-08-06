package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.webService.WebService
import org.json.JSONArray
import org.json.JSONObject

class CreditDebitReportRepository(private val webService: WebService) {
    private val creditDebitReportLiveData = MutableLiveData<Response<JSONArray>>()

    val creditDebitReportData: LiveData<Response<JSONArray>>
        get() = creditDebitReportLiveData

    suspend fun getReport(
        sessionKey: String,
        apiKey: String,
        from: String,
        to: String,
        serviceName: String,
        userType: String
    ) {
        creditDebitReportLiveData.postValue(Response.Loading())

        val result =
            webService.getReports(sessionKey, apiKey, from, to, serviceName, userType)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    val data = responseObject.getJSONArray("Data")
                    creditDebitReportLiveData.postValue(Response.Success(data))
                } else {
                    val message = responseObject.getString("Message")
                    creditDebitReportLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                creditDebitReportLiveData.postValue(Response.Error(result.message(), ""))

            }
        } catch (e: Exception) {
            creditDebitReportLiveData.postValue(e.localizedMessage?.let { Response.Error(it, "") })
        }

    }
}