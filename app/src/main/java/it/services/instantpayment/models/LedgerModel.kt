package it.services.instantpayment.models

data class LedgerModel(
    val amount: String,
    val oldBalance: String,
    val newBalance: String,
    val txnType: String,
    val date: String,
    val remarks: String,
    val crDrType: String
)
