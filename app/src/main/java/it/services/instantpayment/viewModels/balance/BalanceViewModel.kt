package it.services.instantpayment.viewModels.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.BalanceRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BalanceViewModel(private val repository: BalanceRepository) : ViewModel() {

    fun getBalance(sessionKey: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getBalance(sessionKey, apiKey)
        }
    }

    val balanceData :LiveData<Response<String>>
        get()=repository.balanceData
}