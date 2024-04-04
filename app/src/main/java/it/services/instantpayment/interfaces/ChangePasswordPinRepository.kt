package it.services.instantpayment.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.repository.Response
import it.services.instantpayment.webService.WebService

class ChangePasswordPinRepository(private val webService: WebService) {
    private val changePasswordLiveData=MutableLiveData<Response<String>>()
    val changePasswordData:LiveData<Response<String>>
        get() = changePasswordLiveData

    suspend fun changePasswordPin()
    {

    }
}