package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.utils.Prefs

class HostViewModel(private val photoStore: PhotoStore,
                    private val prefs: Prefs) : ViewModel(), OnMapReadyCallback {

    private fun getAllUserPhotosList() = photoStore.getAllUserPhotosList(prefs.getUserEmail())

    lateinit var drawer: MapDrawer

    lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL//устанавливаем тип
        val setUI = googleMap.uiSettings
        setUI.isCompassEnabled = true
        setUI.isMyLocationButtonEnabled = true
        // Add a marker in Sydney and move the camera

        Thread {
            val list = getAllUserPhotosList()
            for (photo in list) {
                val lat = photo.latitude
                val lon = photo.longitude
                if (lat != 0.0 && lon != 0.0) {
                    val latLen = LatLng(photo.latitude, photo.longitude)
                    drawer.drawMarker(latLen)
                }
            }
        }.start()
    }

    interface MapDrawer {
        fun drawMarker(latLen: LatLng)
    }
}