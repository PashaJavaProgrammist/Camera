package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.widget.ImageView
import android.widget.ProgressBar

interface ImageLoader {

    fun loadRoundImageIntoView(imageView: ImageView, uri: String)

    fun loadFullScreenImageIntoView(imageView: ImageView, progressBar: ProgressBar, uri: String)

    fun loadImageInGallery(imageView: ImageView, progressBar: ProgressBar, uri: String)
}