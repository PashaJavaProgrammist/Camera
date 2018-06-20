package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.haretskiy.pavel.magiccamera.convertToLong
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class HostViewModel(private val photoStore: PhotoStore,
                    private val prefs: Prefs,
                    private val router: Router) : ViewModel(), OnMapReadyCallback {

    private fun getAllUserPhotosList() = photoStore.getAllUserPhotosListASC(prefs.getUserEmail())

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
                    drawer.drawMarker(latLen, photo.uri, photo.date)
                }
            }
        }.start()

        mMap.setOnMarkerClickListener {
            router.startPhotoDetailActivity(it.title, it.snippet.convertToLong())
            true
        }
    }

    fun setCameraCore(coreID: Int) {
        prefs.setCameraCoreId(coreID)
    }

    interface MapDrawer {
        fun drawMarker(latLen: LatLng, uri: String, date: Long)
    }
}