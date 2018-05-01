package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.media.Image
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.util.Log
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.storage.Store
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ImageSaverImpl(private val context: Context,
                     private val toaster: Toaster,
                     private val prefs: Prefs,
                     private val store: Store) : ImageSaver {

    override fun createFile() = File(context.getExternalFilesDir(null), "${prefs.getUserEmail()}_${System.currentTimeMillis()}$PIC_FILE_NAME")

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun saveImage(image: Image, file: File) {
        Handler().post({
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            var output: FileOutputStream? = null
            try {
                output = FileOutputStream(file).apply {
                    write(bytes)
                }
                prefs.saveLastPhotoUri(prefs.getUserEmail(), file.absolutePath)
                store.savePhoto(file.absolutePath, System.currentTimeMillis(), prefs.getUserEmail())
                toaster.showToast("$SUCCESSFUL_SAVING$file", false)
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
            } finally {
                image.close()
                output?.let {
                    try {
                        it.close()
                    } catch (e: IOException) {
                        Log.e(TAG, e.toString())
                    }
                }
            }
        })
    }

    override fun saveImage(data: ByteArray) {
        Handler().post({
            try {
                val file = createFile()
                val fos = FileOutputStream(file)
                fos.write(data)
                fos.close()
                prefs.saveLastPhotoUri(prefs.getUserEmail(), file.absolutePath)
                store.savePhoto(file.absolutePath, System.currentTimeMillis(), prefs.getUserEmail())
                toaster.showToast("$SUCCESSFUL_SAVING$file$SIZE_FILE${file.length() / 1024}", false)
            } catch (e: Exception) {
                toaster.showToast("$ERROR_SAVING${e.message}", false)
                e.printStackTrace()
            }
        })
    }

    override fun deleteFile(uri: String) {
        val userEmail = prefs.getUserEmail()
        Handler().post({
            try {
                val file = File(uri)
                file.delete()
                store.deletePhoto(uri)
                if (uri == prefs.getLastPhotoUri(userEmail)) {
                    prefs.saveLastPhotoUri(userEmail, EMPTY_STRING)
                }
                toaster.showToast(SUCCESSFUL_DELETING, false)
            } catch (ex: Exception) {
                toaster.showToast("${ex.message}", false)
            }
        })
    }
}