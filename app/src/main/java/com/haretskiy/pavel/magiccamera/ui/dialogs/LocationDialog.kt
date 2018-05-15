package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.haretskiy.pavel.magiccamera.R


class LocationDialog : DialogFragment() {

    private var answerListener: AnswerListener = object : AnswerListener {
        override fun onConfirm() {}
        override fun onDismiss() {}
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.add_location))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        answerListener.onConfirm()
                        dismiss()
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        answerListener.onDismiss()
                        dismiss()
                    }
                    .create()


    fun show(manager: FragmentManager, tag: String, answerListener: AnswerListener) {
        this.answerListener = answerListener
        show(manager, tag)
    }

    interface AnswerListener {
        fun onConfirm()
        fun onDismiss()
    }
}