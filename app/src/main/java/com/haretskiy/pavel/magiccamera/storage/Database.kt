package com.haretskiy.pavel.magiccamera.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.haretskiy.pavel.magiccamera.models.BarCode
import com.haretskiy.pavel.magiccamera.models.Photo

@Database(entities = [Photo::class, BarCode::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun photoStoreDao(): PhotoStoreDao

    abstract fun barCodeDao(): BarCodeStoreDao

}