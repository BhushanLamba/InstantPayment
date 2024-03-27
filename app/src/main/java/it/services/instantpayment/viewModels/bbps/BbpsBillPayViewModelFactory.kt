package it.services.instantpayment.viewModels.bbps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.BbpsBillPayRepository

class BbpsBillPayViewModelFactory(val repository: BbpsBillPayRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BbpsBillPayViewModel(repository) as T
    }
}