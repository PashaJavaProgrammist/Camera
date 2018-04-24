package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.widget.Toast

class Toaster(private val context: Context) {

    fun showToast(message: String, isLong: Boolean) {
        Toast.makeText(context, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }
}