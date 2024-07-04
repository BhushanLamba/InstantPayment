package it.services.instantpayment.viewModels.dmt.senderVerification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.Response
import it.services.instantpayment.repository.SenderVerificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SenderMobileVerificationViewModel(private val repository: SenderVerificationRepository):ViewModel() {

    fun verifySender(sessionKey:String,apiKey:String,mobileNumber:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.verifySender(sessionKey,apiKey,mobileNumber)
        }
    }

    val verifySenderData:LiveData<Response<JSONObject>>
        get() = repository.verificationData

    fun sendOtp(mobile:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendOtp(mobile)
        }
    }

    val sendOtpData:LiveData<Response<JSONObject>>
        get()=repository.sendOtpData
}