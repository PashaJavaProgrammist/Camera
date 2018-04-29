package com.haretskiy.pavel.magiccamera.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.haretskiy.pavel.magiccamera.models.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun storeDao(): PhotoStoreDao
}