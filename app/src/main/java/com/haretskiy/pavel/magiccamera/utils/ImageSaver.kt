package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.media.Image
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.util.Log
import com.haretskiy.pavel.magiccamera.PIC_FILE_NAME
import com.haretskiy.pavel.magiccamera.TAG
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ImageSaver(private val context: Context, private val toaster: Toaster) {

    fun createFile() = File(context.getExternalFilesDir(null), "${System.currentTimeMillis()}$PIC_FILE_NAME")

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun saveImageApi2(image: Image, file: File) {
        Handler().post({
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

    fun saveImageApi1(data: ByteArray) {
        Handler().post({
            try {
                val file = createFile()
                val fos = FileOutputStream(file)
                fos.write(data)
                fos.close()
                toaster.showToast("Saved: $file", false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }
}