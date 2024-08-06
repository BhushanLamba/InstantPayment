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
        @Field("MPIN") mpin: String,
        @Field("APIResponse") APIResponse: String
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
    @POST("HSenderinfo")
    suspend fun verifySenderFino(
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
    @POST("HAddSender")
    suspend fun addSenderFino(
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("SenderName") SenderName: String,
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
    @POST("HBeneInfo")
    suspend fun getBeneListFino(
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
    @POST("HAddBene")
    suspend fun addBeneUpi(
        @Field("SenderMobile") SenderMobile: String,
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("SenderId") SenderId: String,
        @Field("BeneName") BeneName: String,
        @Field("AccountNo") AccountNo: String,
        @Field("IFSCCode") IFSCCode: String,
        @Field("BankName") BankName: String,
        @Field("AVStatus") AVStatus: String

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
        @Field("MPIN") MPIN: String

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
    @POST
    suspend fun upiAccountVerify(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("SenderMobile") senderMobile: String,
        @Field("BeneName") BeneName: String,
        @Field("UPIID") UPIID: String,
        @Field("IfscCode") IfscCode: String
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
        @Field("MPIN") mpin: String,
        @Field("SenderName") SenderName: String

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
    suspend fun initiateUpiPayment(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("Amount") Amount: String,
        @Field("MobileNo") MobileNo: String,
        @Field("EmailId") EmailId: String,
        @Field("CustomerName") CustomerName: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun updatePayment(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("orderid") orderid: String,
        @Field("comresponse") comresponse: String,
        @Field("txnStatus") txnStatus: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun getAdminBanks(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun fundRequest(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("BankId") BankId: String,
        @Field("Amount") Amount: String,
        @Field("Usertype") Usertype: String,
        @Field("BankName") BankName: String,
        @Field("TxnId") TxnId: String,
        @Field("TxnSlip") TxnSlip: String,
        @Field("DepositMode") DepositMode: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun fundRequestReport(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("DateFrom") DateFrom: String,
        @Field("DateTo") DateTo: String,
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun gatewayReport(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("From") From: String,
        @Field("To") To: String,
        @Field("ServiceName") ServiceName: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun getLedger(
        @Url url: String,
        @Field("SessionKey") sessionKey: String,
        @Field("APIKey") apiKey: String,
        @Field("From") From: String,
        @Field("To") To: String,
        @Field("ServiceName") ServiceName: String
    ): Response<JsonObject>


    @FormUrlEncoded
    @POST
    suspend fun sendOtpPasswordPin(
        @Url url: String,
        @Field("Phone") Phone: String,
        @Field("EmailId") EmailId: String,
        @Field("PanCard") PanCard: String
    ): Response<JsonObject>


    //ADDED NewPassword & NewTxnPin in same api to use same function
    @FormUrlEncoded
    @POST
    suspend fun changePasswordPin(
        @Url url: String,
        @Field("UserId") UserId: String,
        @Field("OTP") OTP: String,
        @Field("NewPassword") NewPassword: String,
        @Field("NewTxnPin") NewTxnPin: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("SendOTP")
    suspend fun sendOtp(@Field("Mobile") Mobile: String): Response<JsonObject>

    @FormUrlEncoded
    @POST("InitiateMATM")
    suspend fun initiateMatm(
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("Amount") Amount: String,
        @Field("service") service: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("AddUsers")
    suspend fun addUser(
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("LoginUserName") LoginUserName: String,
        @Field("ShopAddress") ShopAddress: String,
        @Field("ShopState") ShopState: String,
        @Field("ShopCity") ShopCity: String,
        @Field("ShipZipcode") ShipZipcode: String,
        @Field("CompanyName") CompanyName: String,
        @Field("ProfilePic") ProfilePic: String,
        @Field("CustomerName") CustomerName: String,
        @Field("EmailId") EmailId: String,
        @Field("Phone") Phone: String,
        @Field("Password") Password: String,
        @Field("PanCard") PanCard: String,
        @Field("AadharCard") AadharCard: String,
        @Field("AddressLine1") AddressLine1: String,
        @Field("AddressLine2") AddressLine2: String,
        @Field("State") State: String,
        @Field("City") City: String,
        @Field("Pincode") Pincode: String,
        @Field("Pancopy") Pancopy: String,
        @Field("AadharFront") AadharFront: String,
        @Field("AadharBack") AadharBack: String,
        @Field("Usertype") Usertype: String,
        @Field("RegUsertype") RegUsertype: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("ViewUser")
    suspend fun viewUser(
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("UserType") UserType: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("DebitCredit")
    suspend fun creditDebitUser(
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("TxnType") TxnType: String,
        @Field("TransferUserid") TransferUserid: String,
        @Field("Amount") Amount: String,
        @Field("MPIN") MPIN: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST("PReports")
    suspend fun getReports(
        @Field("SessionKey") SessionKey: String,
        @Field("APIKey") APIKey: String,
        @Field("From") From: String,
        @Field("To") To:String,
        @Field("ServiceName") ServiceName:String,
        @Field("UserType") UserType:String
    ): Response<JsonObject>
}