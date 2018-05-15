package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.haretskiy.pavel.magiccamera.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var latitude = 0.0
    var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        latitude = intent.getDoubleExtra(BUNDLE_KEY_LATITUDE, 0.0)
        longitude = intent.getDoubleExtra(BUNDLE_KEY_LONGITUDE, 0.0)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLen = LatLng(latitude, longitude)
        val cameraPosition = CameraPosition.Builder()
                .target(latLen)
                .zoom(ZOOM_VAL)
                .tilt(TILT_VAL)
                .build()
        val camUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        mMap.addMarker(MarkerOptions().position(latLen))
        mMap.moveCamera(camUpdate)
    }
}
