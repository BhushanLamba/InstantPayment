package it.services.instantpayment.viewModels.creditDebitReport

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.CreditDebitReportRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.launch
import org.json.JSONArray

class CreditDebitReportViewModel(private val repository: CreditDebitReportRepository) : ViewModel() {
    val creditDebitReportData:LiveData<Response<JSONArray>>
        get() = repository.creditDebitReportData

    fun getCreditDebitReport( sessionKey: String,
                              apiKey: String,
                              from: String,
                              to: String,
                              serviceName: String,
                              userType: String)
    {
        viewModelScope.launch {
            repository.getReport(sessionKey, apiKey, from, to, serviceName, userType)
        }
    }
}