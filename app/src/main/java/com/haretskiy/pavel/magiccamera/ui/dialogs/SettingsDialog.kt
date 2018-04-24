package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.CODE_REQUEST_CAMERA_PERMISSION
import com.haretskiy.pavel.magiccamera.navigation.Router
import org.koin.android.ext.android.inject


class SettingsDialog : DialogFragment() {

    val router: Router by inject()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage("Needs to receive permissions manually in settings")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        router.startSettingsActivity()
                    }
                    .create()
}