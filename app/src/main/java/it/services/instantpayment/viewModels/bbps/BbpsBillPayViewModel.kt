package it.services.instantpayment.viewModels.bbps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.BbpsBillPayRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class BbpsBillPayViewModel(private val repository: BbpsBillPayRepository): ViewModel() {

    fun payBill(sessionKey:String,apiKey:String,serviceId:String,operatorId:String,mobileNo:String,accountNumber:String
                ,amount:String,provider:String,dueDate:String,mpin:String,apiResponse:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.payBill(sessionKey, apiKey, serviceId, operatorId, mobileNo, accountNumber, amount, provider, dueDate,mpin,apiResponse)
        }
    }

    val bbpsBillPayData:LiveData<Response<JSONObject>>
        get() = repository.bbpsBillPayData
}