package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.models.BankModel
import it.services.instantpayment.ui.dmt.BeneListFragment
import it.services.instantpayment.ui.dmt.SenderMobileVerificationFragment
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.ApiKeys.ADD_BENE_UPI_KEY
import it.services.instantpayment.utils.ApiKeys.VERIFY_BENE_KEY
import it.services.instantpayment.utils.ApiKeys.VERIFY_BENE_UPI_KEY
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class AddBeneRepository(private val webService: WebService) {
    private val addBeneLiveData = MutableLiveData<Response<JSONObject>>()
    private val verifyBeneLiveData = MutableLiveData<Response<String>>()

    val addBeneData: LiveData<Response<JSONObject>>
        get() = addBeneLiveData

    val verifyBeneData: LiveData<Response<String>>
        get() = verifyBeneLiveData

    private val bankLiveData = MutableLiveData<Response<ArrayList<BankModel>>>()
    val bankData: LiveData<Response<ArrayList<BankModel>>>
        get() = bankLiveData

    suspend fun addBene(
        sessionKey: String,
        apiKey: String,
        senderMobile: String,
        ifsc: String,
        accountNo: String,
        bankName: String,
        beneName: String
    ) {
        addBeneLiveData.postValue(Response.Loading())
        val result =
            if (SenderMobileVerificationFragment.sType.equals("UPI", true)
                ||SenderMobileVerificationFragment.sType.equals("DMT3", true)) {
                /*webService.addBeneUpi(
                    sessionKey,
                    ADD_BENE_UPI_KEY,
                    senderMobile,
                    ifsc,
                    accountNo,
                    bankName,
                    beneName,
                    SenderMobileVerificationFragment.sType
                )*/
                webService.addBeneUpi(senderMobile,sessionKey,"AddBene001",
                    BeneListFragment.SENDER_ID,beneName,accountNo,ifsc,bankName,"YES"
                )

            }
            /*else if(SenderMobileVerificationFragment.sType.equals("DMT3", true))
            {
                webService.addBeneUpi(senderMobile,sessionKey,"AddBene001",SenderMobileVerificationFragment
                )
            }*/

            else {
                webService.addBene(
                    sessionKey,
                    apiKey,
                    senderMobile,
                    ifsc,
                    accountNo,
                    bankName,
                    beneName,
                    SenderMobileVerificationFragment.sType
                )

            }


        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    addBeneLiveData.postValue(Response.Success(responseObject))
                } else {
                    val message = responseObject.getString("Message")
                    addBeneLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                addBeneLiveData.postValue(Response.Error("Try again later", ""))
            }
        } catch (e: Exception) {
            addBeneLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }

    suspend fun verifyBene(
        sessionKey: String,
        apiKey: String,
        senderMobile: String,
        ifsc: String,
        accountNo: String,
        bankName: String,
        beneName: String
    ) {
        verifyBeneLiveData.postValue(Response.Loading())
        val result = if (SenderMobileVerificationFragment.sType.equals("UPI", true)) {


            webService.upiAccountVerify(
                ApiKeys.BASE_URL + "UPIAccountVarify",
                sessionKey,
                VERIFY_BENE_UPI_KEY,
                senderMobile, beneName, accountNo, ifsc
            )
        } else {

            webService.accountVerify(
                ApiKeys.BASE_URL + "AccountVarify",
                sessionKey,
                VERIFY_BENE_KEY,
                senderMobile,
                ifsc,
                accountNo,
                bankName,
                beneName
            )
        }

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    val dataArray = responseObject.getJSONArray("Data")
                    val dataObject = dataArray.getJSONObject(0)
                    val beneName = dataObject.getString("BeneName")

                    verifyBeneLiveData.postValue(Response.Success(beneName))
                } else {
                    val message = responseObject.getString("Message")
                    verifyBeneLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                verifyBeneLiveData.postValue(Response.Error("Try again later", ""))
            }
        } catch (e: Exception) {
            verifyBeneLiveData.postValue(e.localizedMessage?.let { Response.Error(it, "") })
        }
    }

    suspend fun getBanks(sessionKey: String, apiKey: String) {
        bankLiveData.postValue(Response.Loading())

        val result = webService.getBank(sessionKey, apiKey, SenderMobileVerificationFragment.sType)

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {

                    val dataArray = responseObject.getJSONArray("Data")
                    val bankList = ArrayList<BankModel>()

                    for (position in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(position)
                        val bankName = dataObject.getString("Bankname")
                        val ifsc = dataObject.getString("Ifsc")
                        val bankId = dataObject.getString("BankId")
                        val bankImage = dataObject.getString("Picture")

                        val bankModel = BankModel(bankName, ifsc, bankId,bankImage)
                        bankList.add(bankModel)
                    }
                    bankLiveData.postValue(Response.Success(bankList))
                } else {
                    val message = responseObject.getString("Message")
                    bankLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                bankLiveData.postValue(Response.Error("Try again later", ""))
            }
        } catch (e: Exception) {
            bankLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }
}