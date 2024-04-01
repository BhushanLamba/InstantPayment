package it.services.instantpayment.models

data class GatewayReportModel(val amount:String,val orderId:String,val date:String,val status:String)