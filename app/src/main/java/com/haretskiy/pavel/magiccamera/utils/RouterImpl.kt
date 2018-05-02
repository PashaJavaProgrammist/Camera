package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.content.FileProvider
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.ui.activities.BarcodeScanResultActivity
import com.haretskiy.pavel.magiccamera.ui.activities.HostActivity
import com.haretskiy.pavel.magiccamera.ui.activities.LoginActivity
import com.haretskiy.pavel.magiccamera.ui.activities.PhotoDetailActivity
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import java.io.File


class RouterImpl(private val context: Context) : Router {

    override fun startCameraActivity() {
        val intent = Intent(context, HostActivity::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun startLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun startSettingsActivity() {
        val openSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("$PACKAGE_SETTINGS${context.packageName}"))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            openSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(openSettingsIntent)
    }

    override fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int) {
        val ft = fragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
        ft.commit()
    }

    override fun startPhotoDetailActivity(uri: String, date: Long) {
        val intent = Intent(context, PhotoDetailActivity::class.java)
        intent.putExtra(BUNDLE_KEY_URI_TO_ACTIVITY_DETAIL, uri)
        intent.putExtra(BUNDLE_KEY_DATA_TO_DETAIL, date)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun startBarcodeActivity(resultOfScanning: String) {
        val intent = Intent(context, BarcodeScanResultActivity::class.java)
        intent.putExtra(BUNDLE_KEY_BARCODE_RESULT, resultOfScanning)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun shareText(resultOfScanning: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, resultOfScanning)
        sendIntent.type = SHARE_TYPE_TEXT
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(sendIntent)
    }

    override fun shareImage(imageUri: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = SHARE_TYPE_IMAGE
        val uri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, File(imageUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(shareIntent)
    }

    override fun openCustomTabs(uri: String) {
        val builder = CustomTabsIntent.Builder().apply {
            setToolbarColor(getColor(context, R.color.colorPrimary))
            setSecondaryToolbarColor(getColor(context, R.color.colorAccent))
            setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            setExitAnimations(context, android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        val customTabsIntent = builder.build()
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(context, Uri.parse(uri))
    }
}