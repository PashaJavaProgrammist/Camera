package com.haretskiy.pavel.magiccamera.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.haretskiy.pavel.magiccamera.KEY_BUNDLE_TOKEN
import com.haretskiy.pavel.magiccamera.ui.activities.CameraActivity
import com.haretskiy.pavel.magiccamera.ui.activities.LoginActivity

class RouterImpl(private val context: Context) : Router {

    override fun goToCameraActivity(idToken: String) {
        val intent = Intent(context, CameraActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(KEY_BUNDLE_TOKEN, idToken)
        context.startActivity(intent)
    }

    override fun goToLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
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