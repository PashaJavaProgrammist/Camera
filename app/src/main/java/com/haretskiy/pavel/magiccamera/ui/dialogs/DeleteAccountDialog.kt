package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.R
import org.koin.android.ext.android.inject

/**
 * Shows OK/Cancel confirmation dialog about camera permission.
 */
class DeleteAccountDialog : DialogFragment() {

    private val mAuth: FirebaseAuth by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.acc_will_deleted))
                    .setPositiveButton(R.string.yes) { _, _ ->
                        mAuth.currentUser?.delete()
                        dismiss()
                    }
                    .setNegativeButton(R.string.no) { _, _ ->
                        dismiss()
                    }
                    .create()
}