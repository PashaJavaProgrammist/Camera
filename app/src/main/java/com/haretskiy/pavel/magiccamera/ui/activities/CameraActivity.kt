package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.TabViewPagerAdapter
import com.haretskiy.pavel.magiccamera.ui.fragments.Camera2Fragment
import com.haretskiy.pavel.magiccamera.ui.fragments.GalleryFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.QRFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_camera.*
import org.koin.android.ext.android.inject

class CameraActivity : AppCompatActivity() {

    private val qrFragment: QRFragment by inject()
    private val cameraFragment: Camera2Fragment by inject()
    private val galleryFragment: GalleryFragment by inject()
    private val settingsFragment: SettingsFragment by inject()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_gallery -> {
                doTransaction(galleryFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                doTransaction(cameraFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr -> {
                doTransaction(qrFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                doTransaction(settingsFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun doTransaction(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_for_fragments, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
        ft.commit()
    }

}
