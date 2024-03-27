package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import it.services.instantpayment.ui.dmt.SenderMobileVerificationFragment
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.utils.ApiKeys.SENDER_VERIFY_UPI_KEY
import it.services.instantpayment.webService.WebService
import org.json.JSONObject
import retrofit2.Call

class SenderVerificationRepository(private val webService: WebService) {
    private val verificationLiveData = MutableLiveData<Response<JSONObject>>()

    val verificationData: LiveData<Response<JSONObject>>
        get() = verificationLiveData

    suspend fun verifySender(sessionKey: String, apiKey: String, mobileNumber: String) {
        verificationLiveData.postValue(Response.Loading())

        val result:retrofit2.Response<JsonObject>
        if (SenderMobileVerificationFragment.sType.equals("UPI",false))
        {
            result = webService.verifySenderUpi(
                ApiKeys.BASE_URL+ "UPICheckSender",
                sessionKey,
                SENDER_VERIFY_UPI_KEY,
                mobileNumber
            )

        }
        else
        {
            result = webService.verifySender(
                sessionKey,
                apiKey,
                mobileNumber,
                SenderMobileVerificationFragment.sType
            )

        }

        try {
            if(result.body()!=null)
            {
                val responseObject=JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    verificationLiveData.postValue(Response.Success(responseObject))
                }
                else{
                    val message = responseObject.getString("Message")
                    verificationLiveData.postValue(Response.Error(message, statusCode))
                }
            }
            else
            {
                verificationLiveData.postValue(Response.Error("Try again later", ""))
            }
        } catch (e: Exception) {
            verificationLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }
}