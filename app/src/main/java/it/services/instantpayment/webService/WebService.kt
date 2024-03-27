package it.services.instantpayment.webService

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface WebService {

    @FormUrlEncoded
    @POST("UserLogin")
    suspend fun loginUser(
        @Field("UserName") UserName: String,
        @Field("Password") Password: String,
        @Field("lat") lat: String,
        @Field("long") long: String,
        @Field("ApiKey") ApiKey: String,
        @Field("DeviceID") DeviceID: String,
        @Field("TokenKey") TokenKey: String,
        @Field("DeviceInfo") DeviceInfo: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("GetOperator")
    suspend fun getOperator(
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("ServiceId") ServiceId: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("GetbbpsOperator")
    suspend fun getBbpsOperator(
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("ServiceId") ServiceId: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("MainBalance")
    suspend fun getBalance(
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("Recharge")
    suspend fun doRecharge(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("ServiceId") ServiceId: String,
        @Field("OperatorId") OperatorId: String,
        @Field("Account") Account: String,
        @Field("Amount") Amount: String,
        @Field("MPIN") mpin: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("BillFetch")
    suspend fun fetchBill(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("OpId") OpId: String,
        @Field("Accountno") Accountno: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("BillPayment")
    suspend fun payBill(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("ServiceId") ServiceId: String,
        @Field("OperatorId") OperatorId: String,
        @Field("Mobileno") Mobileno: String,
        @Field("Accountno") Accountno: String,
        @Field("Amount") Amount: String,
        @Field("Provider") Provider: String,
        @Field("duedate") duedate: String,
        @Field("MPIN") mpin: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("CheckSender")
    suspend fun verifySender(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("Stype") sType: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("SenderRegistraion")
    suspend fun addSender(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("FirstName") firstName: String,
        @Field("LastName") lastName: String,
        @Field("Address") address: String,
        @Field("Pincode") pinCode: String,
        @Field("Stype") sType: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("SenderValidateOTP")
    suspend fun verifySenderRegistration(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("OTP") otp: String,
        @Field("state") state: String,
        @Field("Stype") sType: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("BeneList")
    suspend fun getBeneList(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("Stype") sType: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("BeneRegistraion")
    suspend fun addBene(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("IfscCode") ifscCode: String,
        @Field("AccountNo") accountNo: String,
        @Field("BankName") bankName: String,
        @Field("BeneName") beneName: String,
        @Field("Stype") sType: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("MoneyTransfer")
    suspend fun payBene(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("Sendermobile") senderMobile: String,
        @Field("BeneName") beneName: String,
        @Field("AccountNo") accountNo: String,
        @Field("IfscCode") ifscCode: String,
        @Field("BankName") bankName: String,
        @Field("BeneId") beneId: String,
        @Field("Amount") amount: String,
        @Field("DMTTYPE") sType: String,
        @Field("TXNMode") TXNMode: String,
        @Field("MPIN") MPIN:String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun accountVerify(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("IfscCode") ifscCode: String,
        @Field("AccountNo") accountNo: String,
        @Field("BankName") bankName: String,
        @Field("BeneName") beneName: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("GetBank")
    suspend fun getBank(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("Stype") sType: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun verifySenderUpi(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun addSenderUpi(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("FirstName") firstName: String,
        @Field("LastName") lastName: String,
        @Field("Address") address: String,
        @Field("Pincode") pinCode: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun verifySenderRegistrationUpi(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("OTP") otp: String,
        @Field("state") state: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("UPIBeneRegistraion")
    suspend fun addBeneUpi(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("IfscCode") ifscCode: String,
        @Field("UPIId") UPIId: String,
        @Field("BankName") bankName: String,
        @Field("BeneName") beneName: String,
        @Field("Stype") sType: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun getBeneListUpi(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun payBeneUPI(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("Sendermobile") senderMobile: String,
        @Field("BeneName") beneName: String,
        @Field("AccountNo") accountNo: String,
        @Field("IfscCode") ifscCode: String,
        @Field("BankName") bankName: String,
        @Field("BeneId") beneId: String,
        @Field("Amount") amount: String,
        @Field("Stype") sType: String,
        @Field("MPIN") mpin: String

    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun getReports(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("From") From: String,
        @Field("To") to: String,
        @Field("ServiceName") serviceName: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun getReceipt(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("TxnId") TxnId: String,
        @Field("ServiceName") ServiceName: String
    ): Response<JsonObject>


    @FormUrlEncoded
    @POST
    suspend fun initiatePayment(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("Amount") Amount: String,
        @Field("MobileNo") MobileNo: String,
        @Field("PancardNo") PancardNo: String,
        @Field("AadharCardNo") AadharCardNo: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun updatePayment(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("orderid") orderid: String,
        @Field("comresponse") comresponse: String,
        @Field("txnStatus") txnStatus:String
    ): Response<JsonObject>
}