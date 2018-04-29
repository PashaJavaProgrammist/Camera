package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.haretskiy.pavel.magiccamera.models.ImageModel

@Dao
interface PhotoStoreDao {

    @get:Query("SELECT * FROM images")
    val all: LiveData<List<ImageModel>>

    @Insert
    fun insert(imageModel: ImageModel)

    @Query("SELECT * FROM images WHERE id = :id")
    fun getPhotoById(id: Long): LiveData<ImageModel>

    @Query("SELECT * FROM images WHERE userEmail = :userMail")
    fun getUsersPhotos(userMail: String): LiveData<List<ImageModel>>
}