package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_FRAGMENT_ID
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.ui.fragments.GalleryFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.GoogleVisionFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.QrHistoryFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.SettingsFragment
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import kotlinx.android.synthetic.main.activity_camera.*
import org.koin.android.ext.android.inject

class HostActivity : AppCompatActivity() {

    private var navFragId = R.id.navigation_camera

    private val router: Router by inject()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_gallery -> {
                navFragId = R.id.navigation_gallery
                router.doFragmentTransaction(GalleryFragment(), supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                navFragId = R.id.navigation_camera
                router.doFragmentTransaction(GoogleVisionFragment(), supportFragmentManager, R.id.frame_for_fragments)
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
        setContentView(R.layout.activity_camera)

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

}
