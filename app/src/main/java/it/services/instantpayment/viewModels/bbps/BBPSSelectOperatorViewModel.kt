package it.services.instantpayment.viewModels.bbps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.services.instantpayment.models.OperatorModel

class BBPSSelectOperatorViewModel:ViewModel() {
    val operatorData=MutableLiveData<OperatorModel>()
}