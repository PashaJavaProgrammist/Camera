package com.haretskiy.pavel.magiccamera.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_DATE_TO_FRAGMENT_DETAIL
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_URI_TO_FRAGMENT_DETAIL
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.ui.fragments.PhotoDetailFragment

class PhotoDetailViewPagerAdapter(
        supportFragmentManager: FragmentManager,
        private val list: List<Photo>) : FragmentStatePagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        args.putString(BUNDLE_KEY_URI_TO_FRAGMENT_DETAIL, list[position].uri)
        args.putLong(BUNDLE_KEY_DATE_TO_FRAGMENT_DETAIL, list[position].date)
        val frag = PhotoDetailFragment()
        frag.arguments = args
        return frag
    }

    override fun getCount() = list.size

}
