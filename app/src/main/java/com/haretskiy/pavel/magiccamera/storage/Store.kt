package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import com.haretskiy.pavel.magiccamera.models.ImageModel

interface Store {

    fun savePhoto(uri: String, date: Long)
    fun getAllPhotosList(): LiveData<List<ImageModel>>
    fun getPhotoById(id: Long): LiveData<ImageModel>
}