package it.services.instantpayment.viewModels.operator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.OperatorRepository

class OperatorVideoModelFactory(private val repository: OperatorRepository):ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OperatorViewModel(repository)as T
    }
}