package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class ViewUserRepository(private val webService: WebService) {
    private val viewUserLiveData=MutableLiveData<Response<JSONObject>>()
    val viewUserData:LiveData<Response<JSONObject>>
        get() = viewUserLiveData


    suspend fun viewUser()
    {
        viewUserLiveData.postValue(Response.Loading())


    }

}