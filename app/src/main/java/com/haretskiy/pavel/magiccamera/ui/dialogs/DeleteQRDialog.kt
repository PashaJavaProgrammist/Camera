package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_IS_QR_DETAIL
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_QR_CODE
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import org.koin.android.ext.android.inject

class DeleteQRDialog : DialogFragment() {

    private val barCodeStore: BarCodeStore by inject()

    private var content: String = EMPTY_STRING
    private var isQRDetail = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(BUNDLE_DIALOG_DELETE_QR_CODE, EMPTY_STRING) ?: EMPTY_STRING
        isQRDetail = arguments?.getBoolean(BUNDLE_DIALOG_DELETE_IS_QR_DETAIL, false) ?: false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.delete_code))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        if (content.isNotEmpty()) barCodeStore.deleteByCode(content)
                        dismiss()
                        if (isQRDetail) activity?.finish()
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        dismiss()
                    }
                    .create()
}