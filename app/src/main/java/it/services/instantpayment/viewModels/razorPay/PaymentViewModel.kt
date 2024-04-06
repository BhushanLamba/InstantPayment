package it.services.instantpayment.viewModels.razorPay

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.PaymentRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel(private val repository:PaymentRepository) : ViewModel() {
    fun initiatePayment(sessionKey: String, apiKey: String,amount:String,mobileNo:String,panCard:String,
                        aadhaarNo:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initiatePayment(sessionKey, apiKey, amount, mobileNo, panCard, aadhaarNo)
        }
    }

    fun updatePayment(endPoint:String,sessionKey: String, apiKey: String,orderId:String,response:String,status:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePayment(endPoint,sessionKey, apiKey, orderId, response,status)
        }
    }

    val paymentData:LiveData<Response<String>>
        get() = repository.paymentData

    val updatePaymentData:LiveData<Response<String>>
        get() = repository.updatePaymentData
}