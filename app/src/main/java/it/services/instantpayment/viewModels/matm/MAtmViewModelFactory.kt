package it.services.instantpayment.viewModels.matm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.MatmRepository

class MAtmViewModelFactory(val repository: MatmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MatmViewModel(repository) as T
    }
}