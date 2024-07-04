package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import it.services.instantpayment.ui.dmt.SenderMobileVerificationFragment
import it.services.instantpayment.utils.ApiKeys
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class AddSenderRepository(private val webService: WebService) {

    private val addSenderLiveData = MutableLiveData<Response<JSONObject>>()

    val addSenderData: LiveData<Response<JSONObject>>
        get() = addSenderLiveData

    private val verifySenderLiveData = MutableLiveData<Response<JSONObject>>()


    val verifySenderData: LiveData<Response<JSONObject>>
        get() = verifySenderLiveData


    suspend fun addSender(
        sessionKey: String,
        apiKey: String,
        mobileNumber: String,
        firstName: String,
        lastName: String,
        address: String,
        pinCode: String
    ) {
        addSenderLiveData.postValue(Response.Loading())
        val result: retrofit2.Response<JsonObject> =

            if (SenderMobileVerificationFragment.sType.equals("UPI", true)) {
                /*webService.addSenderUpi(
                    ApiKeys.BASE_URL + "UPISenderRegistraion",
                    sessionKey,
                    ApiKeys.ADD_SENDER_UPI_KEY,
                    mobileNumber,
                    firstName,
                    lastName,
                    address,
                    pinCode
                )*/
                webService.addSenderFino(
                    sessionKey,
                    "Addsender001",
                    mobileNumber,
                    "$firstName $lastName",
                    lastName,
                    address,
                    pinCode,
                    SenderMobileVerificationFragment.sType
                )

            }
            else if(SenderMobileVerificationFragment.sType.equals("DMT3",true))
            {
                webService.addSenderFino(
                    sessionKey,
                    "Addsender001",
                    mobileNumber,
                    "$firstName $lastName",
                    lastName,
                    address,
                    pinCode,
                    SenderMobileVerificationFragment.sType
                )
            }

            else {
                webService.addSender(
                    sessionKey,
                    apiKey,
                    mobileNumber,
                    firstName,
                    lastName,
                    address,
                    pinCode,
                    SenderMobileVerificationFragment.sType
                )
            }

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    addSenderLiveData.postValue(Response.Success(responseObject))
                } else {
                    val message = responseObject.getString("Message")
                    addSenderLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                addSenderLiveData.postValue(Response.Error("Try again later", ""))
            }
        } catch (e: Exception) {
            addSenderLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }

    suspend fun verifySender(
        sessionKey: String,
        apiKey: String,
        mobileNumber: String,
        otp: String,
        state: String
    ) {
        verifySenderLiveData.postValue(Response.Loading())

        val result: retrofit2.Response<JsonObject> =
            if (SenderMobileVerificationFragment.sType.equals("UPI", false)) {
                webService.verifySenderRegistrationUpi(
                    ApiKeys.BASE_URL + "UPI2AccountVarify",
                    sessionKey, ApiKeys.VALIDATE_SENDER_UPI_KEY, mobileNumber, otp, state
                )
            } else {
                webService.verifySenderRegistration(
                    sessionKey, apiKey, mobileNumber, otp, state,
                    SenderMobileVerificationFragment.sType
                )
            }




        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                if (statusCode.equals("1")) {
                    verifySenderLiveData.postValue(Response.Success(responseObject))
                } else {
                    val message = responseObject.getString("Message")
                    verifySenderLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                verifySenderLiveData.postValue(Response.Error("Try again later", ""))
            }
        } catch (e: Exception) {
            verifySenderLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }
    }
}