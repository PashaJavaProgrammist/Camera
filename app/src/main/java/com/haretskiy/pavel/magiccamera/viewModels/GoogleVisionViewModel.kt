package com.haretskiy.pavel.magiccamera.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import com.haretskiy.pavel.magiccamera.utils.LocationService

class GoogleVisionViewModel(app: Application,
                            private val locationService: LocationService) : AndroidViewModel(app) {

    var progressFlag = false
    val locationData: MutableLiveData<Location> = MutableLiveData()

    fun requestLocation() {
        progressFlag = true
        locationService.requestLocationUpdates(getApplication(), object : LocationService.LocationResultListener {
            override fun onLocationReceived(location: Location) {
                locationData.postValue(location)
                progressFlag = false
            }
        })
    }

}