package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.ui.fragments.*
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import kotlinx.android.synthetic.main.activity_host.*
import org.koin.android.ext.android.inject

class HostActivity : AppCompatActivity() {

    private var navFragId = R.id.navigation_camera

    private val router: Router by inject()
    private val prefs: Prefs by inject()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_gallery -> {
                navFragId = R.id.navigation_gallery
                router.doFragmentTransaction(GalleryFragment(), supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                navFragId = R.id.navigation_camera

                val fragment = when (prefs.getCameraCoreId()) {
                    CAMERA_VISION_CORE -> GoogleVisionFragment()
                    CAMERA_API1_CORE -> CameraFragment()
                    CAMERA_API2_CORE -> Camera2Fragment()
                    else -> GoogleVisionFragment()
                }

                router.doFragmentTransaction(fragment, supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr_history -> {
                navFragId = R.id.navigation_qr_history
                router.doFragmentTransaction(QrHistoryFragment(), supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = navFragId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BUNDLE_KEY_FRAGMENT_ID, navFragId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    fun selectItemCamera() {
        navigation.selectedItemId = R.id.navigation_camera
    }

}
