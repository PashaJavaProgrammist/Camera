package com.haretskiy.pavel.magiccamera.ui.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_URI_TO_ACTIVITY_DETAIL
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.PhotoDetailViewPagerAdapter
import com.haretskiy.pavel.magiccamera.viewModels.PhotoDetailViewModel
import kotlinx.android.synthetic.main.activity_photo_detail.*
import org.koin.android.ext.android.inject

class PhotoDetailActivity : AppCompatActivity() {

    private val photoDetailViewModel: PhotoDetailViewModel by inject()

    private var uri = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        uri = intent.getStringExtra(BUNDLE_KEY_URI_TO_ACTIVITY_DETAIL)

        photoDetailViewModel.storagePhotosLiveData.observe(this, Observer {
            if (it != null) {
                view_pager_details.adapter = PhotoDetailViewPagerAdapter(supportFragmentManager, it)
                for ((i, item) in it.withIndex()) {
                    if (item.uri == uri) view_pager_details.setCurrentItem(i, false)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onPause() {
        super.onPause()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

