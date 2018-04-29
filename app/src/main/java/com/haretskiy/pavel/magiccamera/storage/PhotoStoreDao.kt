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

}