package it.services.instantpayment.viewModels.paymentRequest

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import it.services.instantpayment.models.AdminBanksModel
import it.services.instantpayment.repository.PaymentRequestRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class PaymentRequestViewModel(val repository: PaymentRequestRepository) : ViewModel() {

    val adminBankData:LiveData<Response<ArrayList<AdminBanksModel>>>
        get() = repository.adminBankData

    val paymentRequestData:LiveData<Response<JSONObject>>
        get() = repository.paymentRequestData


    fun getAdminBanks()
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAdminBanks()
        }
    }

    fun doPaymentRequest(bankId:String,amount:String,bankName:String,txnId:String,txnSlip:String,depositMode:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.doPaymentRequest(bankId,amount,bankName,txnId, txnSlip, depositMode)
        }
    }

}