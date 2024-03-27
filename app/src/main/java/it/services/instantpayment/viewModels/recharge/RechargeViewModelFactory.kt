package it.services.instantpayment.viewModels.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.RechargeRepository

class RechargeViewModelFactory(private val repository:RechargeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RechargeViewModel(repository)as T
    }
}