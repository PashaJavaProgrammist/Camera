package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.navigation.Router
import com.haretskiy.pavel.magiccamera.ui.fragments.GalleryFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.SettingsFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.Camera2Fragment
import com.haretskiy.pavel.magiccamera.ui.fragments.CameraFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.QRFragment
import kotlinx.android.synthetic.main.activity_camera.*
import org.koin.android.ext.android.inject

class CameraActivity : AppCompatActivity() {

    private val qrFragment: QRFragment by inject()
    private val camera2Fragment: Camera2Fragment by inject()
    private val cameraFragment: CameraFragment by inject()
    private val galleryFragment: GalleryFragment by inject()
    private val settingsFragment: SettingsFragment by inject()
    private val router: Router by inject()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_gallery -> {
                router.doFragmentTransaction(galleryFragment, supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    router.doFragmentTransaction(camera2Fragment, supportFragmentManager, R.id.frame_for_fragments)
//                } else {
                    router.doFragmentTransaction(cameraFragment, supportFragmentManager, R.id.frame_for_fragments)
//                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr -> {
                router.doFragmentTransaction(qrFragment, supportFragmentManager, R.id.frame_for_fragments)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                router.doFragmentTransaction(settingsFragment, supportFragmentManager, R.id.frame_for_fragments)
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

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
