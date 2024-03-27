package it.services.instantpayment.viewModels.dmt.addSender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.AddSenderRepository

class AddSenderViewModelFactory(private val repository: AddSenderRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddSenderViewModel(repository) as T
    }
}