package it.services.instantpayment.viewModels.operator

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.models.OperatorModel
import it.services.instantpayment.repository.OperatorRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class OperatorViewModel(private val repository: OperatorRepository) : ViewModel() {


    var isNavigate=false
    fun getOperator(sessionKey: String, apiKey: String, serviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getOperator(sessionKey, apiKey, serviceId)
        }
    }

    fun getBbpsOperator(sessionKey: String, apiKey: String, serviceId: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getBbpsOperator(sessionKey,apiKey, serviceId)
        }
    }

    val operatorData: LiveData<Response<ArrayList<OperatorModel>>>
        get() = repository.operatorData

    val bbpsOperatorData:LiveData<Response<ArrayList<OperatorModel>>>
        get()=repository.bbpsOperatorData
}