package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.haretskiy.pavel.magiccamera.models.Photo

@Dao
interface PhotoStoreDao {

    @get:Query("SELECT * FROM images")
    val all: DataSource.Factory<Int, Photo>

    @Insert
    fun insert(photo: Photo)

    @Query("SELECT * FROM images WHERE id = :id")
    fun getPhotoById(id: Long): LiveData<Photo>

    @Query("SELECT * FROM images WHERE uri = :uri")
    fun getPhotoByUri(uri: String): LiveData<Photo>

    @Query("SELECT * FROM images WHERE userEmail = :userMail")
    fun getUsersPhotos(userMail: String): DataSource.Factory<Int, Photo>

    @Query("DELETE FROM images WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM images WHERE uri = :uri")
    fun deleteByUri(uri: String)

    @Delete
    fun deletePhoto(model: Photo)

    @Query("DELETE FROM images")
    fun deleteAll()
}