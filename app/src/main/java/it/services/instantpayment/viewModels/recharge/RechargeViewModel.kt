package it.services.instantpayment.viewModels.recharge

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.RechargeRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class RechargeViewModel(private val repository: RechargeRepository) : ViewModel() {
    fun doRecharge(
        sessionKey: String,
        apiKey: String,
        serviceId: String,
        operatorId: String,
        account: String,
        amount: String,
        mpin: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.doRecharge(sessionKey, apiKey, serviceId, operatorId, account, amount,mpin)
        }
    }

    val rechargeData: LiveData<Response<JSONObject>>
        get() = repository.rechargeData
}