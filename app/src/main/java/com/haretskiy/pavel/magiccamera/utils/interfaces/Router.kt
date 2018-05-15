package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.widget.TextView

interface Router {

    fun startHostActivity()

    fun startLoginActivity()

    fun startSettingsActivity()

    fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int)

    fun doFragmentTransaction(fragment: android.app.Fragment, fragmentManager: android.app.FragmentManager?, containerId: Int)

    fun startPhotoDetailActivity(uri: String, date: Long)

    fun startLocationSettingsActivity()

    fun shareText(resultOfScanning: String)

    fun shareImage(imageUri: String)

    fun shareImages(uris: ArrayList<String>)

    fun openCustomTabs(uri: String)

    fun startScanningActivity(uri: String)

    fun startBarcodeActivity(resultOfScanning: String, date: String)

    fun startMapActivity(latitude: Double, longitude: Double)

    fun startBarcodeActivityWithAnimation(context: Context, activity: FragmentActivity, content: String, date: String, contentView: TextView, dateView: TextView)

}