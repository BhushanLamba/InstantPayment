package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.MainActivity
import it.services.instantpayment.models.GatewayReportModel
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class PaymentGatewayReportRepository(val webService: WebService)
{
    private val reportLiveData=MutableLiveData<Response<ArrayList<GatewayReportModel>>>()

    val reportData:LiveData<Response<ArrayList<GatewayReportModel>>>
        get() = reportLiveData
    private lateinit var reportList: ArrayList<GatewayReportModel>


    suspend fun getGatewayReports(fromDate:String,toDate:String,service:String)
    {
        val response=webService.gatewayReport(ApiKeys.BASE_URL+"PaymentGatewayReport",MainActivity.LOGIN_SESSION,
        ApiKeys.PAYMENT_GATEWAY_REPORT_KEY,fromDate,toDate,service)

        try {
            if (response.isSuccessful) {
                val responseObject = JSONObject(response.body().toString())
                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1", true)) {
                    val dataArray = responseObject.getJSONArray("Data")

                    reportList = ArrayList()
                    for (position in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(position)

                        val amount = dataObject.getString("Amount")
                        val orderId = dataObject.getString("OrderId")
                        var date = dataObject.getString("ReqDate")
                        val status = dataObject.getString("Status")
                        val txnId = dataObject.getString("txn_Id")
                        val panCard = dataObject.getString("Pancard")

                        date=date.split("T")[0]


                        val model = GatewayReportModel(amount,orderId, date, status,txnId,panCard)

                        reportList.add(model)

                        reportLiveData.postValue(Response.Success(reportList))
                    }
                } else {
                    reportLiveData.postValue(Response.Error("Please try after some time.", "0"))

                }


            } else {
                reportLiveData.postValue(Response.Error("Please try after some time.", "0"))
            }
        } catch (e: Exception) {
            reportLiveData.postValue(Response.Error("Please try after some time.", "0"))
        }
    }
}