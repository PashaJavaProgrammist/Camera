package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

interface Router {

    fun startHostActivity()

    fun startLoginActivity()

    fun startSettingsActivity()

    fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int)

    fun startPhotoDetailActivity(uri: String, date: Long)

    fun startBarcodeActivity(resultOfScanning: String)

    fun shareText(resultOfScanning: String)

    fun shareImage(imageUri: String)

    fun openCustomTabs(uri: String)
}