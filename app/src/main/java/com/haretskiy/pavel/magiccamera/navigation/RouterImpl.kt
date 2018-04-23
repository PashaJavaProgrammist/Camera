package com.haretskiy.pavel.magiccamera.navigation

import android.content.Context
import android.content.Intent
import com.haretskiy.pavel.magiccamera.KEY_BUNDLE_TOKEN
import com.haretskiy.pavel.magiccamera.ui.activities.CameraActivity

class RouterImpl(private val context: Context) : Router {

    override fun goToCameraActivity(idToken: String) {
        val intent = Intent(context, CameraActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(KEY_BUNDLE_TOKEN, idToken)
        context.startActivity(intent)
    }
}