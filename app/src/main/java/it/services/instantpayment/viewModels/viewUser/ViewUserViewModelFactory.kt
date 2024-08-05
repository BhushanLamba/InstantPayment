package it.services.instantpayment.viewModels.viewUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.ViewUserRepository

class ViewUserViewModelFactory(val repository: ViewUserRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewUserViewModel(repository) as T
    }
}