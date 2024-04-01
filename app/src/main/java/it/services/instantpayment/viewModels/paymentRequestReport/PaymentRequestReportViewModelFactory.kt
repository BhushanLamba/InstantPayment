package it.services.instantpayment.viewModels.paymentRequestReport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.PaymentRequestReportRepository

class PaymentRequestReportViewModelFactory(private val paymentRequestReportRepository: PaymentRequestReportRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentRequestReportViewModel(paymentRequestReportRepository) as T
    }
}