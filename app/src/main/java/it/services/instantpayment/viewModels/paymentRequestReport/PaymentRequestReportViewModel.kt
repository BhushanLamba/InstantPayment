package it.services.instantpayment.viewModels.paymentRequestReport

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.models.PaymentRequestReportModel
import it.services.instantpayment.repository.PaymentRequestReportRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentRequestReportViewModel(val repository: PaymentRequestReportRepository) : ViewModel()  {

    val reportData:LiveData<Response<ArrayList<PaymentRequestReportModel>>>
        get() = repository.reportData

    fun getPaymentRequestReport(fromDate:String,toDate:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPaymentRequestReport(fromDate,toDate)
        }
    }
}