package it.services.instantpayment.viewModels.upiGateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import it.services.instantpayment.MainActivity
import it.services.instantpayment.repository.Response
import it.services.instantpayment.repository.UpiGatewayRepository
import it.services.instantpayment.utils.ApiKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpiGatewayViewModel(val repository: UpiGatewayRepository) : ViewModel() {

    val paymentData:LiveData<Response<String>>
        get() = repository.paymentData

    fun initiatePayment(amount:String,mobile:String,emailId:String,customerName:String)
    {
         viewModelScope.launch(Dispatchers.IO) {
             repository.initiatePayment(MainActivity.LOGIN_SESSION,ApiKeys.INITIATE_PAYMENT_UPI_KEY,amount,mobile,emailId, customerName)
         }
    }

}