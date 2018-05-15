package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.storage.ShareContainer
import com.haretskiy.pavel.magiccamera.ui.fragments.*
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import com.haretskiy.pavel.magiccamera.viewModels.HostViewModel
import kotlinx.android.synthetic.main.activity_host.*
import org.koin.android.ext.android.inject


class HostActivity : AppCompatActivity() {

    private var navFragId = R.id.navigation_camera
    private val shareContainer: ShareContainer by inject()
    private val router: Router by inject()
    private val prefs: Prefs by inject()
    private val hostViewModel: HostViewModel by inject()


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_gallery -> {
                navFragId = R.id.navigation_gallery
                router.doFragmentTransaction(GalleryFragment(), supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_maps -> {
                navFragId = R.id.navigation_maps
                val mapFragment = MapFragment()
                setMapDrawer()
                mapFragment.getMapAsync(hostViewModel)
                router.doFragmentTransaction(mapFragment, fragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                navFragId = R.id.navigation_camera
                shareContainer.clearContainer()
                val fragment = when (prefs.getCameraCoreId()) {
                    CAMERA_VISION_CORE -> GoogleVisionFragment()
                    CAMERA_API2_CORE -> Camera2Fragment()
                    else -> GoogleVisionFragment()
                }
                router.doFragmentTransaction(fragment, supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr_history -> {
                shareContainer.clearContainer()
                navFragId = R.id.navigation_qr_history
                router.doFragmentTransaction(QrHistoryFragment(), supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                shareContainer.clearContainer()
                navFragId = R.id.navigation_settings
                router.doFragmentTransaction(SettingsFragment(), supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        navFragId = savedInstanceState?.getInt(BUNDLE_KEY_FRAGMENT_ID, R.id.navigation_camera) ?: R.id.navigation_camera

    }

    override fun onResume() {
        super.onResume()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = navFragId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BUNDLE_KEY_FRAGMENT_ID, navFragId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        shareContainer.clearContainer()
        finishAffinity()
    }

    fun selectItemCamera() {
        navigation.selectedItemId = R.id.navigation_camera
    }

    private fun setMapDrawer() {
        hostViewModel.drawer = object : HostViewModel.MapDrawer {
            override fun drawMarker(latLen: LatLng, uri: String, date: Long) {
                runOnUiThread {
                    val cameraPosition = CameraPosition.Builder()
                            .target(latLen)
                            .zoom(ZOOM_VAL)
                            .tilt(TILT_VAL)
                            .build()
                    val camUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
                    hostViewModel.mMap.addMarker(MarkerOptions().position(latLen).title(uri).snippet(date.convertToDate()))
                    hostViewModel.mMap.moveCamera(camUpdate)
                }
            }
        }
    }

}
