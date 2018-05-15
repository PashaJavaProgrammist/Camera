package com.haretskiy.pavel.magiccamera.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.haretskiy.pavel.magiccamera.TURN_ON_GPS
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class LocationService(private val locationManager: LocationManager,
                      private val toaster: Toaster,
                      private val router: Router) : LocationListener {

    var listener = object : LocationResultListener {
        override fun onLocationReceived(location: Location) {}
    }

    fun requestLocationUpdates(context: Context, listener: LocationResultListener) {
        this.listener = listener
        val permission = context.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
        if (permission == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100L, 5f, this)
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 100L, 5f,
                    this)
        }
    }

    fun removeUpdates() {
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        listener.onLocationReceived(location)
        removeUpdates()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {
        toaster.showToast(TURN_ON_GPS, false)
        router.startLocationSettingsActivity()
    }

    interface LocationResultListener {
        fun onLocationReceived(location: Location)
    }

}