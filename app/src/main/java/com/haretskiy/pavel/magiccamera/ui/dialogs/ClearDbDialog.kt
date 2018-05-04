package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.utils.ImageSaverImpl
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import org.koin.android.ext.android.inject


class ClearDbDialog : DialogFragment() {

    private val barCodeStore: BarCodeStore by inject()
    private val imageSaver: ImageSaver by inject()
    private val prefs: Prefs by inject()
    private val toaster: Toaster by inject()

    private var type = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getString(BUNDLE_KEY_TYPE_CLEAR_DB, EMPTY_STRING) ?: EMPTY_STRING
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(
                            when (type) {
                                TYPE_QR -> getString(R.string.delete_all_sc_codes)
                                TYPE_PHOTO -> getString(R.string.delete_all_photos)
                                else -> EMPTY_STRING
                            })
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        when (type) {
                            TYPE_QR -> {
                                barCodeStore.deleteAllUserCodes(prefs.getUserEmail())
                                toaster.showToast(getString(R.string.all_codes_delete), false)
                            }
                            TYPE_PHOTO -> {
                                imageSaver.deleteAllUserPhotos(prefs.getUserEmail(), object : ImageSaverImpl.DeletingListener {
                                    override fun onSuccess() {
                                        toaster.showToast(SUCCESSFUL_ALL_DELETING, false)
                                    }

                                    override fun onError(errorMessage: String) {
                                        toaster.showToast(errorMessage, true)
                                    }
                                })
                            }

                        }
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        dismiss()
                    }
                    .create()
}