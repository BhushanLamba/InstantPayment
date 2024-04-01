package it.services.instantpayment.models

data class AdminBanksModel(val bankName:String,val bankId:String,val accountNo:String,val ifsc:String,val charges:String)