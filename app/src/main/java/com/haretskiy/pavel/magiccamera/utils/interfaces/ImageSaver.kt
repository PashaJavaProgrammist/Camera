package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.media.Image
import java.io.File

interface ImageSaver {

    fun createFile(): File
    fun saveImage(data: ByteArray)
    fun saveImage(image: Image, file: File)
}