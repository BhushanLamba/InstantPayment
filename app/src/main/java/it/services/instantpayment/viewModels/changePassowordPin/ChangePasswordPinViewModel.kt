package it.services.instantpayment.viewModels.changePassowordPin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.interfaces.ChangePasswordPinRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ChangePasswordPinViewModel(private val repository: ChangePasswordPinRepository) : ViewModel() {
    val changePasswordData: LiveData<Response<String>>
        get() = repository.changePasswordData

    val sendOtpData:LiveData<Response<JSONObject>>
        get() = repository.sendOtpData


    fun changePasswordPin(url:String,userId: String, inputOtp: String, newPasswordPin: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changePasswordPin(url,userId,inputOtp,newPasswordPin )
        }
    }

    fun sendOtp(url:String,phone:String,email:String,panCard:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendOtp(url,phone, email, panCard)
        }
    }


}