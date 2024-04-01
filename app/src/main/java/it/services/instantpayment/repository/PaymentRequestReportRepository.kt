package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.MainActivity
import it.services.instantpayment.models.PaymentRequestReportModel
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class PaymentRequestReportRepository(val webService: WebService) {

    private val reportLiveData = MutableLiveData<Response<ArrayList<PaymentRequestReportModel>>>()

    val reportData: LiveData<Response<ArrayList<PaymentRequestReportModel>>>
        get() = reportLiveData

    private lateinit var reportList: ArrayList<PaymentRequestReportModel>

    suspend fun getPaymentRequestReport(fromDate: String, toDate: String) {
        val response = webService.fundRequestReport(
            ApiKeys.BASE_URL + "PaymentRequestReport",
            MainActivity.LOGIN_SESSION,
            ApiKeys.PAYMENT_REQUEST_REPORT_KEY,
            fromDate,
            toDate
        )

        try {
            if (response.isSuccessful) {
                val responseObject = JSONObject(response.body().toString())
                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1", true)) {
                    val dataArray = responseObject.getJSONArray("Data")

                    reportList = ArrayList()
                    for (position in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(position)

                        val accountNo = dataObject.getString("Accountno")
                        val bankName = dataObject.getString("BankName")
                        val amount = dataObject.getString("Amount")
                        val charge = dataObject.getString("Charge")
                        val txnId = dataObject.getString("TxnId")
                        val status = dataObject.getString("Status")
                        var reqDate = dataObject.getString("reqdate")
                        reqDate = reqDate.split("T")[0]
                        val remarks = dataObject.getString("AminRemarks")

                        val model = PaymentRequestReportModel(
                            accountNo,
                            bankName,
                            amount,
                            charge,
                            txnId,
                            reqDate,
                            status,
                            remarks
                        )

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