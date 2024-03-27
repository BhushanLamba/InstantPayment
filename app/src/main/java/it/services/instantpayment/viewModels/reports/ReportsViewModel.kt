package it.services.instantpayment.viewModels.reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.models.ReportsModel
import it.services.instantpayment.repository.ReportsRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ReportsViewModel(private val repository: ReportsRepository) :ViewModel()
{
    val reportsListData: LiveData<Response<ArrayList<JSONObject>>>
        get() = repository.reportsListData

    val receiptData:LiveData<Response<JSONObject>>
        get() = repository.receiptData

    fun getReports(sessionKey:String,apiKey:String,serviceName:String,fromDate:String,toDate:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getReports(sessionKey, apiKey,serviceName,fromDate,toDate)
        }
    }

    fun getReceipt(sessionKey:String,apiKey:String,txnId:String,serviceName:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getReceipt(sessionKey, apiKey, txnId, serviceName)
        }
    }
}