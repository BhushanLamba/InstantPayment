package it.services.instantpayment.viewModels.matm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import it.services.instantpayment.repository.MatmRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MatmViewModel(private val mAtmRepository: MatmRepository): ViewModel() {

    val mAtmInitiateData:LiveData<Response<String>>
        get() = mAtmRepository.mAtmInitiateData

    fun initiateMAtmTransaction(amount:String,service:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            mAtmRepository.initiateMAtmTransaction(amount,service)
        }
    }

}