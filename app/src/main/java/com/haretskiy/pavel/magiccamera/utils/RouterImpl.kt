package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_URI_TO_DETAIL
import com.haretskiy.pavel.magiccamera.PACKAGE_SETTINGS
import com.haretskiy.pavel.magiccamera.ui.activities.HostActivity
import com.haretskiy.pavel.magiccamera.ui.activities.LoginActivity
import com.haretskiy.pavel.magiccamera.ui.activities.PhotoDetailActivity
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

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
        context.startActivity(intent)
    }

    override fun startSettingsActivity() {
        val openSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("$PACKAGE_SETTINGS${context.packageName}"))
        context.startActivity(openSettingsIntent)
    }

    override fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int) {
        val ft = fragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
        ft.commit()
    }

    override fun startPhotoDetailActivity(uri: String) {
        val intent = Intent(context, PhotoDetailActivity::class.java)
        intent.putExtra(BUNDLE_KEY_URI_TO_DETAIL, uri)
        context.startActivity(intent)
    }
}