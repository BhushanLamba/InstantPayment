package it.services.instantpayment.viewModels.upiGateway

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.UpiGatewayRepository

class UpiGatewayViewModelFactory(val repository: UpiGatewayRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpiGatewayViewModel(repository) as T
    }
}