package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import com.haretskiy.pavel.magiccamera.models.Photo

interface PhotoStore {

    fun savePhoto(uri: String, date: Long, email: String)
    fun update(photo: Photo)
    fun getAllPhotosDataSourceFactory(): DataSource.Factory<Int, Photo>
    fun getAllUserPhotosDataSourceFactory(userEmail: String): DataSource.Factory<Int, Photo>
    fun getAllUserPhotosLiveData(userEmail: String): LiveData<List<Photo>>
    fun getAllUserPhotosList(userEmail: String): List<Photo>
    fun getAllUserPhotosListASC(userEmail: String): List<Photo>
    fun getPhotoById(id: Long): LiveData<Photo>
    fun getPhotoByUriSync(uri: String): Photo
    fun deletePhoto(uri: String)
    fun deleteAll()
    fun deleteAllUserPhoto(userEmail: String)
    fun deletePhotoSync(uri: String)
}