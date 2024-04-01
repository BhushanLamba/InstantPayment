package it.services.instantpayment.viewModels.paymentRequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.PaymentRequestRepository

class PaymentRequestViewModelFactory(val repository: PaymentRequestRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentRequestViewModel(repository) as T
    }
}