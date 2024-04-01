package it.services.instantpayment.viewModels.ledger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.LedgerRepository

class LedgerViewModelFactory(private val repository: LedgerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LedgerViewModel(repository) as T
    }
}