package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_IS_QR_DETAIL
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_QR_CODE
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.utils.interfaces.DeleteListener
import org.koin.android.ext.android.inject

class DeleteQRDialog : DialogFragment() {

    private val barCodeStore: BarCodeStore by inject()

    private var content: String = EMPTY_STRING
    private var isQRDetail = false
    private var listener: DeleteListener = object : DeleteListener {
        override fun onConfirm() {}
        override fun onDismiss() {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        content = arguments?.getString(BUNDLE_DIALOG_DELETE_QR_CODE, EMPTY_STRING) ?: EMPTY_STRING
        isQRDetail = arguments?.getBoolean(BUNDLE_DIALOG_DELETE_IS_QR_DETAIL, false) ?: false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.delete_code))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        if (content.isNotEmpty()) {
                            barCodeStore.deleteByCode(content)
                            listener.onConfirm()
                        }
                        dismiss()
                        if (isQRDetail) {
                            activity?.finish()
                        }
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        dismiss()
                        listener.onDismiss()
                    }
                    .create()

    fun show(manager: FragmentManager?, tag: String?, listener: DeleteListener) {
        this.listener = listener
        show(manager, tag)
    }
}