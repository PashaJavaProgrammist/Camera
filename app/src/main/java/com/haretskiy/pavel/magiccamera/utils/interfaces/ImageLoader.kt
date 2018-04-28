package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.widget.ImageView

interface ImageLoader {

    fun loadRoundImageIntoView(imageView: ImageView, uri: String)
}