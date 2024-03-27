package it.services.instantpayment.repository

sealed class Response<T>(val data: T? = null, val errMessage: String? = null,val statusCode:String?=null) {
    class Loading<T> : Response<T>()
    class Success<T>(response: T? = null) : Response<T>(data = response)
    class Error<T>(errMessage: String,statusCode: String) : Response<T>(errMessage = errMessage, statusCode = statusCode)
}

