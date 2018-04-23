package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.TabViewPagerAdapter
import com.haretskiy.pavel.magiccamera.ui.fragments.CameraFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.GalleryFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.QRFragment
import kotlinx.android.synthetic.main.activity_camera.*
import org.koin.android.ext.android.inject

class CameraActivity : AppCompatActivity() {

    private val qrFragment: QRFragment by inject()
    private val cameraFragment: CameraFragment by inject()
    private val galleryFragment: GalleryFragment by inject()

    private val viewPagerAdapter = TabViewPagerAdapter(supportFragmentManager)

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_gallery -> {
                pager.setCurrentItem(0, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_camera -> {
                pager.setCurrentItem(1, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr -> {
                pager.setCurrentItem(2, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        initViewPagerAdapter()
    }


    private fun initViewPagerAdapter() {
        viewPagerAdapter.addFragment(galleryFragment, getString(R.string.title_gallery))
        viewPagerAdapter.addFragment(cameraFragment, getString(R.string.title_camera))
        viewPagerAdapter.addFragment(qrFragment, getString(R.string.title_qr))

        pager.adapter = viewPagerAdapter

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                selectItemOnBottomNavigation(position)
            }
        })
    }

    private fun selectItemOnBottomNavigation(position: Int) {
        when (position) {
            0 -> navigation.selectedItemId = R.id.navigation_gallery
            1 -> navigation.selectedItemId = R.id.navigation_camera
            2 -> navigation.selectedItemId = R.id.navigation_qr
        }
    }
}
