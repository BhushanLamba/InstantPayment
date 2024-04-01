package it.services.instantpayment.viewModels.paymentGatewayReport

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import it.services.instantpayment.models.GatewayReportModel
import it.services.instantpayment.repository.PaymentGatewayReportRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentGatewayReportViewModel(val repository:PaymentGatewayReportRepository) : ViewModel()
{
    val reportData:LiveData<Response<ArrayList<GatewayReportModel>>>
        get() = repository.reportData

    fun getGatewayReport(fromDate:String,toDate:String,service:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGatewayReports(fromDate, toDate, service)
        }
    }
}