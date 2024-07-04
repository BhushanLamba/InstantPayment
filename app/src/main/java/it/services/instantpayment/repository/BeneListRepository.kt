package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import it.services.instantpayment.models.BeneListModel
import it.services.instantpayment.ui.dmt.BeneListFragment
import it.services.instantpayment.ui.dmt.SenderMobileVerificationFragment
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONArray
import org.json.JSONObject

class BeneListRepository(private val webService: WebService) {
    private val beneListLiveData = MutableLiveData<Response<ArrayList<BeneListModel>>>()

    val beneListData: LiveData<Response<ArrayList<BeneListModel>>>
        get() = beneListLiveData

    private val payBeneLiveData = MutableLiveData<Response<JSONObject>>()

    val payBeneData: LiveData<Response<JSONObject>>
        get() = payBeneLiveData


    suspend fun getBeneList(sessionKey: String, apiKey: String, mobileNumber: String) {
        beneListLiveData.postValue(Response.Loading())

        val response: retrofit2.Response<JsonObject> =
            if (SenderMobileVerificationFragment.sType.equals("UPI", false)) {
                /*webService.getBeneListUpi(
                    ApiKeys.BASE_URL + "UPIBeneList",
                    sessionKey,
                    ApiKeys.BENE_LIST_UPI_KEY,
                    mobileNumber
                )*/

                webService.getBeneListFino(
                    sessionKey,
                    apiKey,
                    mobileNumber,
                    SenderMobileVerificationFragment.sType
                )

            }
            else if (SenderMobileVerificationFragment.sType.equals("DMT3",true))
            {
                webService.getBeneListFino(
                    sessionKey,
                    apiKey,
                    mobileNumber,
                    SenderMobileVerificationFragment.sType
                )
            }

            else {
                webService.getBeneList(
                    sessionKey,
                    apiKey,
                    mobileNumber,
                    SenderMobileVerificationFragment.sType
                )

            }


        try {
            if (response.isSuccessful) {
                val responseObject = JSONObject(response.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {

                    val dataArrayStr = responseObject.getString("Data")
                    val dataArray = JSONArray(dataArrayStr)
                    val beneList = ArrayList<BeneListModel>()
                    for (position in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(position)
                        var accountNumber=""
                        var ifsc=""
                        var bankName=""
                        var beneId=""
                        var isBankVerified=""
                        var beneName=""
                        /*if (SenderMobileVerificationFragment.sType.equals("UPI", false)) {
                            accountNumber = dataObject.getString("upi_id")
                            beneId = dataObject.getString("benficiary_id")
                            isBankVerified = dataObject.getString("is_bank_verified")
                            beneName = dataObject.getString("name")

                        }
                        else*/
                        if (SenderMobileVerificationFragment.sType.equals("DMT3",true)
                            || SenderMobileVerificationFragment.sType.equals("UPI",true))
                        {
                            ifsc = dataObject.getString("IFSCCode")
                            bankName = dataObject.getString("BankName")
                            accountNumber = dataObject.getString("AccountNo")
                            beneId = dataObject.getString("Id")
                            isBankVerified = dataObject.optString("is_bank_verified")
                            beneName = dataObject.optString("BeneName")
                        }
                        else
                        {
                            ifsc = dataObject.getString("ifsc")
                            bankName = dataObject.getString("bank_name")
                            accountNumber = dataObject.getString("account_number")
                            beneId = dataObject.getString("beneId")
                            isBankVerified = dataObject.getString("is_bank_verified")
                            beneName = dataObject.getString("name")
                        }

                        val beneListModel = BeneListModel(
                            beneName,
                            accountNumber,
                            bankName,
                            ifsc,
                            beneId,
                            isBankVerified
                        )

                        beneList.add(beneListModel)
                    }
                    beneListLiveData.postValue(Response.Success(beneList))
                } else {
                    val message = responseObject.getString("Message")
                    beneListLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                beneListLiveData.postValue(Response.Error(response.message(), ""))
            }
        } catch (e: Exception) {
            beneListLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }

    suspend fun payBene(
        sessionKey: String,
        apiKey: String,
        mobileNumber: String,
        beneName: String,
        accountNo: String,
        ifscCode: String,
        bankName: String,
        beneId: String,
        amount: String,
        mpin: String,
        transactionMode:String
    ) {
        payBeneLiveData.postValue(Response.Loading())

        val response: retrofit2.Response<JsonObject> =
            if (SenderMobileVerificationFragment.sType.equals("UPI", false)
                || SenderMobileVerificationFragment.sType.equals("DMT3", false)) {
                webService.payBeneUPI(
                    ApiKeys.BASE_URL + "UPI2MoneyTransfer",
                    sessionKey,
                    ApiKeys.PAY_BENE_UPI_KEY,
                    mobileNumber,
                    beneName,
                    accountNo,
                    ifscCode,
                    bankName,
                    beneId,
                    amount,
                    SenderMobileVerificationFragment.sType,
                    mpin,
                    BeneListFragment.SENDER_NAME
                )
            }
            else {
                webService.payBene(
                    sessionKey,
                    apiKey,
                    mobileNumber,
                    beneName,
                    accountNo,
                    ifscCode,
                    bankName,
                    beneId,
                    amount,
                    SenderMobileVerificationFragment.sType,transactionMode,mpin
                )
            }

        try {
            if (response.isSuccessful) {
                val responseObject = JSONObject(response.body().toString())

                val statusCode = responseObject.getString("Status_Code")

                if (statusCode.equals("1")) {
                    payBeneLiveData.postValue(Response.Success(responseObject))
                } else {
                    val message = responseObject.getString("Message")
                    payBeneLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                payBeneLiveData.postValue(Response.Error(response.message(), ""))
            }
        } catch (e: Exception) {
            payBeneLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }
}