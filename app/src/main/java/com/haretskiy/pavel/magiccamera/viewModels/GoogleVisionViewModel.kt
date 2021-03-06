package com.haretskiy.pavel.magiccamera.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.location.Location
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.utils.LocationService
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver

class GoogleVisionViewModel(app: Application,
                            private val locationService: LocationService,
                            private val imageSaver: ImageSaver,
                            private val prefs: Prefs,
                            private val photoStore: PhotoStore) : AndroidViewModel(app) {

    var progressFlag = false
    val locationData: MutableLiveData<Location> = MutableLiveData()

    private var uri = EMPTY_STRING

    fun requestLocation() {
        progressFlag = true
        locationService.requestLocationUpdates(getApplication(), object : LocationService.LocationResultListener {
            override fun onLocationReceived(location: Location) {
                locationData.postValue(location)
                progressFlag = false
                uri = prefs.getLastPhotoUri(prefs.getUserEmail())
                addCoordinatesToPhoto(uri, location.latitude, location.longitude)
            }
        })
    }

    fun saveImage(data: ByteArray) {
        imageSaver.saveImage(data)
    }

    private fun addCoordinatesToPhoto(uri: String, latitude: Double, longitude: Double) {
        Thread {
            val photo = photoStore.getPhotoByUriSync(uri)
            photo.latitude = latitude
            photo.longitude = longitude
            photoStore.update(photo)
        }.start()
    }

}