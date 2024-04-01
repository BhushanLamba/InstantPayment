package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import it.services.instantpayment.MainActivity
import it.services.instantpayment.models.AdminBanksModel
import it.services.instantpayment.models.BankModel
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.ApiKeys.ADMIN_BANK_KEY
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class PaymentRequestRepository(val webService: WebService)
{
    private val adminBanksLiveData=MutableLiveData<Response<ArrayList<AdminBanksModel>>>()
    private val paymentRequestLiveData=MutableLiveData<Response<JSONObject>>()

    val adminBankData:LiveData<Response<ArrayList<AdminBanksModel>>>
        get() = adminBanksLiveData

    val paymentRequestData:LiveData<Response<JSONObject>>
        get() = paymentRequestLiveData

    suspend fun getAdminBanks()
    {
        adminBanksLiveData.postValue(Response.Loading())

        val response=webService.getAdminBanks(ApiKeys.BASE_URL+"GetPaymentBank",MainActivity.LOGIN_SESSION,ADMIN_BANK_KEY)

        try {
            if (response.body() != null) {
                val responseObject = JSONObject(response.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    val dataArray=responseObject.getJSONArray("Data")

                    val bankList=ArrayList<AdminBanksModel>()

                    for (position in 0 until dataArray.length())
                    {
                        val dataObject=dataArray.getJSONObject(position)
                        val bankId=dataObject.getString("Id")
                        val bankName=dataObject.getString("BankName")
                        val accountNumber=dataObject.getString("AccountNumber")
                        val ifscCode=dataObject.getString("IFSCCode")
                        val charge=dataObject.getString("charge")

                        val adminBankModel=AdminBanksModel(bankName,bankId,accountNumber,ifscCode,charge)
                        bankList.add(adminBankModel)
                    }

                    adminBanksLiveData.postValue(Response.Success(bankList))


                } else {
                    val message = responseObject.getString("Message")
                    adminBanksLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                adminBanksLiveData.postValue(Response.Error("Try again later", ""))
            }
        }
        catch (e: Exception) {
            adminBanksLiveData.postValue(e.localizedMessage?.let { Response.Error(it, "") })
        }
    }

    suspend fun doPaymentRequest(bankId:String,amount:String,bankName:String,txnId:String,txnSlip:String,depositMode:String) {
        paymentRequestLiveData.postValue(Response.Loading())

        val response=webService.fundRequest(ApiKeys.BASE_URL+"PaymentRequest",MainActivity.LOGIN_SESSION,ApiKeys.PAYMENT_REQUEST_KEY,bankId,amount,"RT",
        bankName,txnId,txnSlip,depositMode)

        try {
            if (response.body() != null) {
                val responseObject = JSONObject(response.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {

                    paymentRequestLiveData.postValue(Response.Success(responseObject))


                } else {
                    val message = responseObject.getString("Message")
                    adminBanksLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                adminBanksLiveData.postValue(Response.Error("Try again later", ""))
            }
        }
        catch (e: Exception) {
            paymentRequestLiveData.postValue(e.localizedMessage?.let { Response.Error(it, "") })
        }
    }
}