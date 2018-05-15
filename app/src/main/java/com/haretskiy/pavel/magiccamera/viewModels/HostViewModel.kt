package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.utils.Prefs

class HostViewModel(private val photoStore: PhotoStore,
                    private val prefs: Prefs) : ViewModel(), OnMapReadyCallback {

    fun getAllUserPhotosLiveData() = photoStore.getAllUserPhotosLiveData(prefs.getUserEmail())

    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL//устанавливаем тип
        val setUI = googleMap.uiSettings
        setUI.isCompassEnabled = true
        setUI.isMyLocationButtonEnabled = true
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}