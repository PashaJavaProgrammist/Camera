package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.haretskiy.pavel.magiccamera.models.Photo

@Dao
interface PhotoStoreDao {

    @get:Query("SELECT * FROM images ORDER BY id DESC")
    val all: DataSource.Factory<Int, Photo>

    @Insert
    fun insert(photo: Photo)

    @Update
    fun update(photo: Photo)

    @Query("SELECT * FROM images WHERE id = :id")
    fun getPhotoById(id: Long): LiveData<Photo>

    @Query("SELECT * FROM images WHERE uri = :uri")
    fun getPhotoByUri(uri: String): LiveData<Photo>

    @Query("SELECT * FROM images WHERE uri = :uri")
    fun getPhotoByUriSync(uri: String): Photo

    @Query("SELECT * FROM images WHERE userEmail = :userMail ORDER BY id DESC")
    fun getUserPhotosDataSourceFactory(userMail: String): DataSource.Factory<Int, Photo>

    @Query("SELECT * FROM images WHERE userEmail = :userMail ORDER BY id DESC")
    fun getUserPhotosLiveDataList(userMail: String): LiveData<List<Photo>>

    @Query("SELECT * FROM images WHERE userEmail = :userMail ORDER BY id DESC")
    fun getUserPhotosList(userMail: String): List<Photo>

    @Query("SELECT * FROM images WHERE userEmail = :userMail ORDER BY id ASC")
    fun getUserPhotosListASC(userMail: String): List<Photo>

    @Query("DELETE FROM images WHERE userEmail = :userMail")
    fun deleteAllUserPhotos(userMail: String)

    @Query("DELETE FROM images WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM images WHERE uri = :uri")
    fun deleteByUri(uri: String)

    @Delete
    fun deletePhoto(model: Photo)

    @Query("DELETE FROM images")
    fun deleteAll()
}