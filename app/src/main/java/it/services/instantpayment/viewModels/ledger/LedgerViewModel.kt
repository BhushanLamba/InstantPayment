package it.services.instantpayment.viewModels.ledger

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.models.LedgerModel
import it.services.instantpayment.repository.LedgerRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LedgerViewModel(val repository: LedgerRepository) : ViewModel() {
    val reportData: LiveData<Response<ArrayList<LedgerModel>>>
        get() = repository.reportData

    fun getLedger(fromDate: String, toDate: String, service: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLedger(fromDate, toDate, service)
        }
    }
}