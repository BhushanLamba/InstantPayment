package it.services.instantpayment.viewModels.razorPay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.PaymentRepository

class PaymentViewModelFactory(private val repository: PaymentRepository) :ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentViewModel(repository) as T
    }
}