package com.haretskiy.pavel.magiccamera.utils

import android.annotation.TargetApi
import android.content.Context
import android.media.Image
import android.os.Build
import android.os.Handler
import android.util.Log
import com.haretskiy.pavel.magiccamera.PIC_FILE_NAME
import com.haretskiy.pavel.magiccamera.TAG
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@TargetApi(Build.VERSION_CODES.KITKAT)
class ImageSaver(private val context: Context) {

    fun createFile() = File(context.getExternalFilesDir(null), "${System.currentTimeMillis()}$PIC_FILE_NAME")

    fun saveImage(image: Image, file: File) {
        val handler = Handler()
        handler.post({
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            var output: FileOutputStream? = null
            try {
                output = FileOutputStream(file).apply {
                    write(bytes)
                }
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
}