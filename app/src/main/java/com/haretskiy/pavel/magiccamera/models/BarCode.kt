package com.haretskiy.pavel.magiccamera.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.haretskiy.pavel.magiccamera.EMPTY_STRING

@Entity(tableName = "barcodes")
data class BarCode(
        @PrimaryKey
        var code: String = EMPTY_STRING,
        var userEmail: String = EMPTY_STRING,
        var date: Long = 0L
)
