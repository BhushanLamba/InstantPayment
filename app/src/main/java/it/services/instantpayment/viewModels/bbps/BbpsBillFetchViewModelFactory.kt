package it.services.instantpayment.viewModels.bbps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.BbpsBillFetchRepository
import it.services.instantpayment.viewModels.recharge.RechargeViewModel

class BbpsBillFetchViewModelFactory(private val repository: BbpsBillFetchRepository):ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BbpsBillFetchViewModel(repository) as T
    }
}