package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.media.Image
import com.haretskiy.pavel.magiccamera.utils.ImageSaverImpl
import java.io.File

interface ImageSaver {

    fun createFile(): File
    fun saveImage(data: ByteArray)
    fun saveImage(image: Image, file: File, listener: ImageSaverImpl.CreatingListener)
    fun deleteFile(uri: String)
    fun deleteAllUserPhotos(email: String, listener: ImageSaverImpl.DeletingListener)
    fun deletePhotos(listOfUris: ArrayList<String>, listener: ImageSaverImpl.DeletingListener)
}