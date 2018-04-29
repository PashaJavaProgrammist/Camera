package com.haretskiy.pavel.magiccamera.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "images")
data class ImageModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var date: Long = 0,
        var uri: String = ""
)