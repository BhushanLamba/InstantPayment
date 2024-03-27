package it.services.instantpayment.models

data class BeneListModel(val beneName:String,val accountNumber:String,val bankName:String,
val ifsc:String,val beneId:String,val isBankVerified:String)
