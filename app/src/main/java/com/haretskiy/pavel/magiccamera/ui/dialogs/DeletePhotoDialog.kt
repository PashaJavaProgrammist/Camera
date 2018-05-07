package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_URI
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.utils.interfaces.DeleteListener
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import org.koin.android.ext.android.inject


class DeletePhotoDialog : DialogFragment() {

    private val imageSaver: ImageSaver by inject()

    private var uri: String = EMPTY_STRING
    private var isPhotoDetail = false
    private var listener: DeleteListener = object : DeleteListener {
        override fun onConfirm() {}
        override fun onDismiss() {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uri = arguments?.getString(BUNDLE_DIALOG_DELETE_URI, EMPTY_STRING) ?: EMPTY_STRING
        isPhotoDetail = arguments?.getBoolean(BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL, false) ?: false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.delete_this_photo))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        if (uri.isNotEmpty()) {
                            imageSaver.deleteFile(uri)
                            listener.onConfirm()
                        }
                        dismiss()
                        if (isPhotoDetail) {
                            activity?.finish()
                        }
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        listener.onDismiss()
                        dismiss()
                    }
                    .create()

    fun show(manager: FragmentManager?, tag: String?, listener: DeleteListener) {
        this.listener = listener
        show(manager, tag)
    }
}