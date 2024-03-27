package it.services.instantpayment.viewModels.dmt.beneList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.models.BeneListModel
import it.services.instantpayment.repository.BeneListRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class BeneListViewModel(private val repository: BeneListRepository) : ViewModel() {


    val beneListData:LiveData<Response<ArrayList<BeneListModel>>>
        get() = repository.beneListData

    val payBeneData:LiveData<Response<JSONObject>>
        get() = repository.payBeneData

    fun getBeneList(sessionKey:String,apiKey:String,mobileNumber:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getBeneList(sessionKey, apiKey, mobileNumber)
        }
    }

    fun payBene(sessionKey: String,apiKey: String,mobileNumber: String,beneName:String,
                accountNo:String,ifscCode:String,bankName:String,beneId:String,amount:String,mpin:String,transactionMode:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.payBene(sessionKey, apiKey, mobileNumber,beneName,accountNo,ifscCode, bankName, beneId, amount,mpin,transactionMode)
        }
    }


}