package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.Toaster
import org.koin.android.ext.android.inject


class ClearDbDialog : DialogFragment() {

    private val barCodeStore: BarCodeStore by inject()
    private val photoStore: PhotoStore by inject()
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
                                photoStore.deleteAllUserPhoto(prefs.getUserEmail())
                                toaster.showToast(getString(R.string.all_phot_dele), false)
                            }

                        }
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        dismiss()
                    }
                    .create()
}