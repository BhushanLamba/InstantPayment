package it.services.instantpayment.viewModels.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.BalanceRepository

class BalanceViewModelFactory(private val balanceRepository: BalanceRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BalanceViewModel(balanceRepository) as T
    }
}