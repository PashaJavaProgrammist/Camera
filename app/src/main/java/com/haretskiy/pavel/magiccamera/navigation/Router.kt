package com.haretskiy.pavel.magiccamera.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

interface Router {

    fun goToCameraActivity(idToken: String)

    fun goToLoginActivity()

    fun startSettingsActivity()

    fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int)
}