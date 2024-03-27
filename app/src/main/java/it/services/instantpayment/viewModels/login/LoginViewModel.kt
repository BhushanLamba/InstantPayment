package it.services.instantpayment.viewModels.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import it.services.instantpayment.repository.LoginRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(private val repository: LoginRepository)
    :ViewModel()
{

    fun loginUser( userName:String, password:String, simNumber:String,token:String, appName:String, lat:String, lng:String,context:Context)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loginUser(userName,password,simNumber,token, appName, lat, lng,context)
        }
    }

    val loginData: LiveData<Response<JSONObject>>
        get() = repository.loginData
}