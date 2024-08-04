package it.services.instantpayment.viewModels.addUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.AddUserRepository

class AddUserVideoModelFactory(private val repository: AddUserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddUserViewModel(repository) as T
    }
}