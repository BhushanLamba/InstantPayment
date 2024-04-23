package it.services.instantpayment.models

data class BankModel(
    val bankName: String,
    val ifsc: String,
    val bankId: String,
    val bankImage: String
)
