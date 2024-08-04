package it.services.instantpayment.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.services.instantpayment.webService.WebService
import org.json.JSONObject

class AddUserRepository(private val webService: WebService) {
    private val addUserLiveData = MutableLiveData<Response<String>>()

    val addUserData: LiveData<Response<String>>
        get() = addUserLiveData

    suspend fun addUser(
        sessionKey: String, apiKey: String, LoginUserName: String, ShopAddress: String,
        ShopState: String, ShopCity: String, ShipZipcode: String, CompanyName: String,
        ProfilePic: String, CustomerName: String, EmailId: String, Phone: String,
        Password: String, PanCard: String, AadharCard: String, AddressLine1: String,
        AddressLine2: String, State: String, City: String, Pincode: String,
        Pancopy: String, AadharFront: String, AadharBack: String, Usertype: String,
        RegUsertype: String
    ) {
        addUserLiveData.postValue(Response.Loading())
        val result = webService.addUser(
            sessionKey, apiKey, LoginUserName, ShopAddress, ShopState, ShopCity,
            ShipZipcode, CompanyName, ProfilePic, CustomerName, EmailId, Phone, Password, PanCard,
            AadharCard, AddressLine1, AddressLine2, State, City, Pincode, Pancopy, AadharFront,
            AadharBack, Usertype, RegUsertype
        )

        try {
            if (result.body() != null) {
                val responseObject = JSONObject(result.body().toString())
                val statusCode = responseObject.getString("Status_Code")
                val message = responseObject.getString("Message")
                if (statusCode.equals("1")) {
                    addUserLiveData.postValue(Response.Success(message))
                } else {
                    addUserLiveData.postValue(Response.Error(message, statusCode))
                }
            } else {
                addUserLiveData.postValue(Response.Error("Try again later", ""))
            }
        } catch (e: Exception) {
            addUserLiveData.postValue(Response.Error(e.localizedMessage, ""))
        }

    }
}