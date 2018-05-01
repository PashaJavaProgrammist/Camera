package com.haretskiy.pavel.magiccamera.ui.views

interface PhotoGallery {

    fun onClickPhoto(uri: String, date: Long)
    fun onLongClickPhoto(uri: String): Boolean
}