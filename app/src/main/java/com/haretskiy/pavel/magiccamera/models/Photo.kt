package com.haretskiy.pavel.magiccamera.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "images")
data class Photo(
        var date: Long = 0,
        var uri: String = "",
        var userEmail: String = "",
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0)