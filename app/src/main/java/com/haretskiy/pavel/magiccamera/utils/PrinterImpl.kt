package com.haretskiy.pavel.magiccamera.utils

import android.app.Activity
import android.content.Context
import android.support.v4.content.FileProvider
import android.support.v4.print.PrintHelper
import com.haretskiy.pavel.magiccamera.FILE_PROVIDER_AUTHORITY
import com.haretskiy.pavel.magiccamera.PRINT
import com.haretskiy.pavel.magiccamera.utils.interfaces.Printer
import java.io.File

class PrinterImpl(private val context: Context, private val toater: Toaster) : Printer {

    override fun printPhoto(activity: Activity, imageUri: String) {
        Thread({
            try {
                val uri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, File(imageUri))
                val photoPrinter = PrintHelper(activity)
                photoPrinter.scaleMode = PrintHelper.SCALE_MODE_FIT
                photoPrinter.printBitmap(PRINT, uri)
            } catch (ex: Exception) {
                toater.showToast(ex.message.toString(), true)
            }
        }).start()
    }
}