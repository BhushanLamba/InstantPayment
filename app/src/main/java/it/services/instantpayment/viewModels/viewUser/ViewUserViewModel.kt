package it.services.instantpayment.viewModels.viewUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.Response
import it.services.instantpayment.repository.ViewUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class ViewUserViewModel(private val repository: ViewUserRepository) : ViewModel() {
    val viewUserData:LiveData<Response<JSONArray>>
        get() = repository.viewUserData

    val creditDebitData:LiveData<Response<String>>
        get() = repository.creditDebitData

    fun viewUser(sessionKey:String,apiKey:String,userType:String)
    {
        viewModelScope.launch{
            repository.viewUser(sessionKey, apiKey, userType)
        }
    }

    fun creditDebitUser(sessionKey: String, apiKey: String,txnType:String,transferUserId:String,
                        amount:String,mpin:String)
    {
        viewModelScope.launch{
            repository.creditDebitUser(sessionKey, apiKey,txnType,transferUserId, amount, mpin)
        }
    }
}