package it.services.instantpayment.viewModels.dmt.senderVerification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.services.instantpayment.repository.SenderVerificationRepository

class SenderMobileVerificationViewModelFactory(private val senderVerificationRepository: SenderVerificationRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SenderMobileVerificationViewModel(senderVerificationRepository) as T
    }
}