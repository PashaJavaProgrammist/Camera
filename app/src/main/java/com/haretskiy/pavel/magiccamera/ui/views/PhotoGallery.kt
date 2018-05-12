package com.haretskiy.pavel.magiccamera.ui.views

import com.haretskiy.pavel.magiccamera.viewModels.GalleryViewModel

interface PhotoGallery {

    fun onClickPhoto(uri: String, date: Long)
    fun onLongClickPhoto(uri: String, listener: GalleryViewModel.OnCheckedListener): Boolean
    fun isPhotoCheckedToShare(uri: String): Boolean
}