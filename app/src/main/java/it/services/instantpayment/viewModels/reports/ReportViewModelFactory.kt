package it.services.instantpayment.viewModels.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.ReportsRepository

class ReportViewModelFactory(private val repository: ReportsRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReportsViewModel(repository) as T
    }
}