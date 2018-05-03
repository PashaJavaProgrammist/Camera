package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeleteQRDialog
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import java.util.regex.Pattern

class QrResultDetailViewModel(private val router: Router,
                              private val toaster: Toaster) : ViewModel() {

    private val pattern1 = Pattern.compile(URL_REGEX1)
    private val pattern2 = Pattern.compile(URL_REGEX2, Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL)

    fun onClickDelete(content: String, fragmentManager: FragmentManager) {
        newDeleteDialogInstance(content).show(fragmentManager, DELETE_QR_DIALOG)
    }

    fun openUrlInApp(scanResult: String) {
        try {
            val m1 = pattern1.matcher(scanResult)
            val m2 = pattern2.matcher(scanResult)
            if (m1.find() || m2.find()) {
                router.openCustomTabs(scanResult)
            } else {
                toaster.showToast(URL_DO_NOT_FOUND, false)
            }
        } catch (ex: Exception) {
            toaster.showToast("$OPEN_CHROME_TABS ${ex.message}", true)
        }
    }

    fun shareUrl(content: String) {
        router.shareText(content)
    }

    private fun newDeleteDialogInstance(content: String): DeleteQRDialog {
        val args = Bundle()
        args.putString(BUNDLE_DIALOG_DELETE_QR_CODE, content)
        args.putBoolean(BUNDLE_DIALOG_DELETE_IS_QR_DETAIL, true)
        val deleteDialog = DeleteQRDialog()
        deleteDialog.arguments = args
        return deleteDialog
    }
}