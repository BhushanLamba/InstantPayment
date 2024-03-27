package it.services.instantpayment.viewModels.dmt.addBene

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.AddBeneRepository

class AddBeneViewModelFactory(private val repository: AddBeneRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddBeneViewModel(repository) as T
    }
}