package it.services.instantpayment.models

data class PaymentRequestReportModel(val accountNo:String,val bankName:String,val amount:String,val charge:String,val txnId:String,
val date:String,val status:String,val remarks:String)
