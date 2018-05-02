package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.haretskiy.pavel.magiccamera.models.Photo

interface PhotoStore {

    fun savePhoto(uri: String, date: Long, email: String)
    fun getAllPhotosDataSourceFactory(): DataSource.Factory<Int, Photo>
    fun getAllUserPhotosDataSourceFactory(userEmail: String): DataSource.Factory<Int, Photo>
    fun getAllUserPhotosLiveData(userEmail: String): LiveData<List<Photo>>
    fun getPhotoById(id: Long): LiveData<Photo>
    fun deletePhoto(uri: String)
    fun deleteAll()
    fun deleteAllUserPhoto(userEmail: String)

}