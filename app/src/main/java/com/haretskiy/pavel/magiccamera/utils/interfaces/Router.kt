package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

interface Router {

    fun goToCameraActivity()

    fun goToLoginActivity()

    fun startSettingsActivity()

    fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int)
}