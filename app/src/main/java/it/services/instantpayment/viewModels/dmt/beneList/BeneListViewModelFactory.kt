package it.services.instantpayment.viewModels.dmt.beneList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.BeneListRepository

class BeneListViewModelFactory(private val repository:BeneListRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BeneListViewModel(repository) as T
    }
}