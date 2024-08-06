package it.services.instantpayment.viewModels.creditDebitReport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.CreditDebitReportRepository

class CreditDebitReportViewModelFactory(private val repository: CreditDebitReportRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreditDebitReportViewModel(repository) as T
    }
}