package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.MainActivity
import it.services.instantpayment.models.LedgerModel
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class LedgerRepository(val webService: WebService) {
    private val reportLiveData=MutableLiveData<Response<ArrayList<LedgerModel>>>()

    val reportData:LiveData<Response<ArrayList<LedgerModel>>>
        get() = reportLiveData

    private lateinit var reportList:ArrayList<LedgerModel>

    suspend fun getLedger(fromDate:String,toDate:String,service:String)
    {
        val response=webService.getLedger(ApiKeys.BASE_URL+"LadgerReport",MainActivity.LOGIN_SESSION,ApiKeys.LEDGER_REPORT_KEY,fromDate,toDate,service)

        try {
            if (response.isSuccessful)
            {
                val responseObject=JSONObject(response.body().toString())

                val statusCode=responseObject.getString("Status_Code")
                if (statusCode.equals("1",true))
                {
                    val dataArray=responseObject.getJSONArray("Data")
                    reportList=ArrayList()
                    for(position in 0 until  dataArray.length())
                    {
                        val dataObject=dataArray.getJSONObject(position)
                        val oldBal=dataObject.getString("OldBal")
                        val amount=dataObject.getString("Amount")
                        val newBal=dataObject.getString("NewBal")
                        val txnType=dataObject.getString("txnType")
                        val crDrType=dataObject.getString("CrdrType")
                        val remarks=dataObject.getString("Remarks")
                        var date=dataObject.getString("txndate")

                        date=date.split("T")[0]

                        val model=LedgerModel(amount,oldBal,newBal,txnType,date,remarks,crDrType)
                        reportList.add(model)

                    }

                    reportLiveData.postValue(Response.Success(reportList))
                }
                else
                {
                    reportLiveData.postValue(Response.Error("",""))
                }
            }
            else
            {
                reportLiveData.postValue(Response.Error("",""))
            }
        }
        catch (e:Exception)
        {
            reportLiveData.postValue(Response.Error("",""))
        }
    }
}