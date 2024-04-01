package it.services.instantpayment.viewModels.paymentGatewayReport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.PaymentGatewayReportRepository

class PaymentGatewayReportViewModelFactory(val repository:PaymentGatewayReportRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentGatewayReportViewModel(repository) as T
    }
}