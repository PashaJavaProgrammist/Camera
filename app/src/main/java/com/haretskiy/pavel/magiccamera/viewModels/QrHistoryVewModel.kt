package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.widget.TextView
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_IS_QR_DETAIL
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_QR_CODE
import com.haretskiy.pavel.magiccamera.CAMERA_VISION_CORE
import com.haretskiy.pavel.magiccamera.DELETE_QR_DIALOG
import com.haretskiy.pavel.magiccamera.models.BarCode
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeleteQRDialog
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.DeleteListener
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router


class QrHistoryVewModel(private val barCodeStore: BarCodeStore,
                        private val prefs: Prefs,
                        private val router: Router) : ViewModel() {

    val storageBarCodesLiveData: LiveData<List<BarCode>> =
            barCodeStore.getAllUserCodes(prefs.getUserEmail())

    fun deleteQrCodeFromDB(fragmentManager: FragmentManager, content: String) {
        newDeleteDialogInstance(content).show(fragmentManager, DELETE_QR_DIALOG)
    }

    fun deleteQrCodeFromDB(fragmentManager: FragmentManager, content: String, listener: DeleteListener) {
        newDeleteDialogInstance(content).show(fragmentManager, DELETE_QR_DIALOG, listener)
    }

    fun startBarcodeActivity(content: String, date: String) {
        router.startBarcodeActivity(content, date)
    }

    private fun newDeleteDialogInstance(content: String): DeleteQRDialog {
        val args = Bundle()
        args.putString(BUNDLE_DIALOG_DELETE_QR_CODE, content)
        args.putBoolean(BUNDLE_DIALOG_DELETE_IS_QR_DETAIL, false)
        val deleteDialog = DeleteQRDialog()
        deleteDialog.arguments = args
        return deleteDialog
    }

    fun turnOnQRDetector() {
        prefs.setCameraCoreId(CAMERA_VISION_CORE)
        prefs.turnOnQRDetector(true)
    }

    fun startBarcodeActivityWithAnimation(context: Context,
                                          activity: FragmentActivity,
                                          content: String,
                                          date: String,
                                          contentView: TextView,
                                          dateView: TextView) {
        router.startBarcodeActivityWithAnimation(
                context,
                activity,
                content,
                date,
                contentView,
                dateView)
    }

}