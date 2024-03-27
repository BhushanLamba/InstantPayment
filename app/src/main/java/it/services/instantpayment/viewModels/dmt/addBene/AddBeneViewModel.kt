package it.services.instantpayment.viewModels.dmt.addBene

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import it.services.instantpayment.models.BankModel
import it.services.instantpayment.repository.AddBeneRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddBeneViewModel(private val repository: AddBeneRepository) : ViewModel() {
    fun addBene(
        sessionKey: String,
        apiKey: String,
        senderMobile: String,
        ifsc: String,
        accountNo: String,
        bankName: String,
        beneName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBene(
                sessionKey,
                apiKey,
                senderMobile,
                ifsc,
                accountNo,
                bankName,
                beneName
            )
        }
    }

    fun verifyBene(
        sessionKey: String,
        apiKey: String,
        senderMobile: String,
        ifsc: String,
        accountNo: String,
        bankName: String,
        beneName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.verifyBene(
                sessionKey,
                apiKey,
                senderMobile,
                ifsc,
                accountNo,
                bankName,
                beneName
            )
        }
    }

    fun getBank(sessionKey: String, apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getBanks(sessionKey, apiKey)
        }
    }

    val addBeneData: LiveData<Response<JSONObject>>
        get() = repository.addBeneData

    val bankData: LiveData<Response<ArrayList<BankModel>>>
        get() = repository.bankData

    val verifyBeneData:LiveData<Response<String>>
        get()=repository.verifyBeneData
}