package it.services.instantpayment.viewModels.bbps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.BbpsBillFetchRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class BbpsBillFetchViewModel(private val repository: BbpsBillFetchRepository) : ViewModel() {
     fun fetchBill(sessionKey: String, apiKey: String, opId: String, accountNo: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchBill(sessionKey, apiKey, opId, accountNo)
        }
    }

    val billFetchData: LiveData<Response<JSONObject>>
        get() = repository.billFetchData
}