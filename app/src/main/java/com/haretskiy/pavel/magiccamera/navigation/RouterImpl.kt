package com.haretskiy.pavel.magiccamera.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.haretskiy.pavel.magiccamera.BuildConfig
import com.haretskiy.pavel.magiccamera.KEY_BUNDLE_TOKEN
import com.haretskiy.pavel.magiccamera.ui.activities.CameraActivity
import com.haretskiy.pavel.magiccamera.ui.activities.LoginActivity

class RouterImpl(private val context: Context) : Router {

    override fun goToCameraActivity() {
        val intent = Intent(context, CameraActivity::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun goToLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }

    override fun startSettingsActivity() {
        val openSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${context.packageName}"))
        context.startActivity(openSettingsIntent)
    }

    override fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int) {
        val ft = fragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
        ft.commit()
    }
}