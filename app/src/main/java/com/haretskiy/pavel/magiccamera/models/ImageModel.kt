package com.haretskiy.pavel.magiccamera.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "images")
data class ImageModel(
        var date: Long = 0,
        var uri: String = "",
        var userEmail: String = "",
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0)