package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.storage.ShareContainer
import com.haretskiy.pavel.magiccamera.utils.ImageSaverImpl
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import org.koin.android.ext.android.inject

class DeletePhotosDialog : DialogFragment() {

    private val shareContainer: ShareContainer by inject()
    private val imageSaver: ImageSaver by inject()

    private var listener: ImageSaverImpl.DeletingPhotoListener = object : ImageSaverImpl.DeletingPhotoListener {
        override fun onSuccess() {}
        override fun onError(errorMessage: String) {}
    }

    private val listOfUris: ArrayList<String> by lazy {
        shareContainer.getAllUris()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.delete_this_photos))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        imageSaver.deletePhotos(listOfUris, listener)
                        dismiss()
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        dismiss()
                    }
                    .create()

    fun show(manager: FragmentManager?, tag: String?, listener: ImageSaverImpl.DeletingPhotoListener) {
        this.listener = listener
        show(manager, tag)
    }
}