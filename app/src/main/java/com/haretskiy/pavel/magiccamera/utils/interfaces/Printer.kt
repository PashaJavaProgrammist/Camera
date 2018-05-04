package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.app.Activity

interface Printer {

    fun printPhoto(activity: Activity, imageUri: String)
}