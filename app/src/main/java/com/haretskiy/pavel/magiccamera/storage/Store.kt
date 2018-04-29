package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.haretskiy.pavel.magiccamera.models.Photo

interface Store {

    fun savePhoto(uri: String, date: Long, email: String)
    fun getAllPhotosList(): DataSource.Factory<Int, Photo>
    fun getAllUserPhotos(userEmail: String): DataSource.Factory<Int, Photo>
    fun getPhotoById(id: Long): LiveData<Photo>
    fun deletePhoto(uri: String)
    fun deleteAll()
    fun deleteAllUserPhoto(userEmail: String)

}