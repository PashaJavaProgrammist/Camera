package com.haretskiy.pavel.magiccamera.utils.interfaces

import android.media.Image
import com.haretskiy.pavel.magiccamera.utils.ImageSaverImpl
import java.io.File

interface ImageSaver {

    fun createFile(): File
    fun saveImage(data: ByteArray)
    fun saveImage(image: Image, file: File, listener: ImageSaverImpl.SavingPhotoListener)
    fun deleteFile(uri: String)
    fun deleteAllUserPhotos(email: String, listener: ImageSaverImpl.DeletingPhotoListener)
    fun deletePhotos(listOfUris: ArrayList<String>, listener: ImageSaverImpl.DeletingPhotoListener)
}