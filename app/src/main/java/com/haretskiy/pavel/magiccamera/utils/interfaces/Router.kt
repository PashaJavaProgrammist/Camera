package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View

interface Router {

    fun startHostActivity()

    fun startLoginActivity()

    fun startSettingsActivity()

    fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int)

    fun startPhotoDetailActivity(uri: String, date: Long)

    fun shareText(resultOfScanning: String)

    fun shareImage(imageUri: String)

    fun openCustomTabs(uri: String)

    fun startScanningActivity(uri: String)

    fun startBarcodeActivity(resultOfScanning: String, date: String)

    fun startBarcodeActivityWithAnimation(activity: AppCompatActivity, resultOfScanning: String, date: String, dateView: View, contentView: View)
}