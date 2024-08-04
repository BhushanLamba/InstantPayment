package it.services.instantpayment.viewModels.addUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.services.instantpayment.repository.AddUserRepository
import it.services.instantpayment.repository.Response
import kotlinx.coroutines.launch

class AddUserViewModel(val repository: AddUserRepository) : ViewModel() {

    val addUserData: LiveData<Response<String>>
        get() = repository.addUserData

    fun addUser(
        sessionKey: String, apiKey: String, LoginUserName: String, ShopAddress: String,
        ShopState: String, ShopCity: String, ShipZipcode: String, CompanyName: String,
        ProfilePic: String, CustomerName: String, EmailId: String, Phone: String,
        Password: String, PanCard: String, AadharCard: String, AddressLine1: String,
        AddressLine2: String, State: String, City: String, Pincode: String,
        Pancopy: String, AadharFront: String, AadharBack: String, Usertype: String,
        RegUsertype: String
    ) {
        viewModelScope.launch {
            repository.addUser(
                sessionKey, apiKey, LoginUserName, ShopAddress, ShopState,
                ShopCity, ShipZipcode, CompanyName, ProfilePic, CustomerName, EmailId, Phone,
                Password, PanCard, AadharCard, AddressLine1, AddressLine2, State, City, Pincode,
                Pancopy, AadharFront, AadharBack, Usertype, RegUsertype
            )
        }
    }

}