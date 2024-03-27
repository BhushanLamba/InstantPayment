package it.services.instantpayment.utils

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationUtils : ViewModel() {

    private var lastLocation: Location? = null

    private val latitudeLiveData = MutableLiveData<String>()
    private val longitudeLiveData = MutableLiveData<String>()

    val latitude: LiveData<String>
        get() = latitudeLiveData

    val longitude: LiveData<String>
        get() = longitudeLiveData


    private var fusedLocationClient: FusedLocationProviderClient? = null


    fun getLocation(context: Context, activity: Activity) {



        if (ActivityCompat.checkSelfPermission(
                context,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, arrayOf(ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION),
                1
            )
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        fusedLocationClient?.lastLocation!!.addOnCompleteListener(activity) {
            if (it.isSuccessful) {
                lastLocation = it.result
                latitudeLiveData.postValue(lastLocation!!.latitude.toString())
                longitudeLiveData.postValue(lastLocation!!.longitude.toString())
            }
        }
    }



}