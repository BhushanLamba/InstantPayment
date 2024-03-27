package it.services.instantpayment.viewModels.dmt.addSender

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.AddSenderRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddSenderViewModel(private val repository: AddSenderRepository): ViewModel() {

    fun addSender(sessionKey:String,apiKey:String,mobileNumber:String,firstName:String,lastName:String,address:String,pinCode:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSender(sessionKey, apiKey, mobileNumber, firstName, lastName, address, pinCode)
        }
    }

    fun verifySender(sessionKey: String,apiKey: String,mobileNumber: String,otp:String,state:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.verifySender(sessionKey, apiKey, mobileNumber, otp,state)
        }
    }

    val addSenderData:LiveData<Response<JSONObject>>
        get() = repository.addSenderData

    val verifySenderData:LiveData<Response<JSONObject>>
        get()=repository.verifySenderData
}