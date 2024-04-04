package it.services.instantpayment.viewModels.changePassowordPin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.interfaces.ChangePasswordPinRepository

class ChangePasswordPinViewModelFactory(val repository: ChangePasswordPinRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChangePasswordPinViewModel(repository) as T
    }
}