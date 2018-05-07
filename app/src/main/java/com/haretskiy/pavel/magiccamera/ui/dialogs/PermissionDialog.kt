package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.haretskiy.pavel.magiccamera.CODE_REQUEST_CAMERA_PERMISSION
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import org.koin.android.ext.android.inject

/**
 * Shows OK/Cancel confirmation dialog about camera permission.
 */
class PermissionDialog : DialogFragment() {

    private val router: Router by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(R.string.request_permission)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        parentFragment?.requestPermissions(arrayOf(Manifest.permission.CAMERA), CODE_REQUEST_CAMERA_PERMISSION)
                    }
                    .setNegativeButton(R.string.settings) { _, _ ->
                        router.startSettingsActivity()
                    }
                    .create()
}