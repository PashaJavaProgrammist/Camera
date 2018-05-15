package com.haretskiy.pavel.magiccamera.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.utils.LocationService
import com.haretskiy.pavel.magiccamera.utils.LocationService.LocationResultListener
import org.koin.android.ext.android.inject


class LocationDialog : DialogFragment() {

    private val locationService: LocationService by inject()

    private var listenerLocation: LocationResultListener = object : LocationResultListener {
        override fun onLocationReceived(location: Location) {}
    }

    private var answerListener: AnswerListener = object : AnswerListener {
        override fun onConfirm() {}
        override fun onDismiss() {}
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.add_location))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        answerListener.onConfirm()
                        context?.let { requestLocation(it) }
                        dismiss()
                    }
                    .setNegativeButton(getString(R.string.no)) { _, _ ->
                        answerListener.onDismiss()
                        dismiss()
                    }
                    .create()


    private fun requestLocation(context: Context) {
        locationService.requestLocationUpdates(context, listenerLocation)
    }

    fun show(manager: FragmentManager, tag: String, locationResultListener: LocationResultListener, answerListener: AnswerListener) {
        this.listenerLocation = locationResultListener
        this.answerListener = answerListener
        show(manager, tag)
    }

    interface AnswerListener {
        fun onConfirm()
        fun onDismiss()
    }
}