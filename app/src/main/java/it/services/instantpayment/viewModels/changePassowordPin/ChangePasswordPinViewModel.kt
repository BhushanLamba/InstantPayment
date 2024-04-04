package it.services.instantpayment.viewModels.changePassowordPin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.interfaces.ChangePasswordPinRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePasswordPinViewModel(private val repository: ChangePasswordPinRepository) : ViewModel() {
    val changePasswordData: LiveData<Response<String>>
        get() = repository.changePasswordData

    fun changePasswordPin()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changePasswordPin()
        }
    }
}