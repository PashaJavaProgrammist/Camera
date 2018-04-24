package com.haretskiy.pavel.magiccamera.ui.base

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.WindowManager
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.ui.dialogs.ConfirmationDialog
import com.haretskiy.pavel.magiccamera.ui.dialogs.ErrorDialog
import com.haretskiy.pavel.magiccamera.ui.dialogs.SettingsDialog
import com.haretskiy.pavel.magiccamera.utils.ImageSaver
import com.haretskiy.pavel.magiccamera.utils.Toaster
import org.koin.android.ext.android.inject

open class BaseCameraFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

    protected val windowManager: WindowManager by inject()
    protected val toaster: Toaster by inject()
    protected val imageSaver: ImageSaver by inject()

    protected fun requestCameraPermission() {
        if (activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, android.Manifest.permission.CAMERA) } == true) {
            ConfirmationDialog().show(childFragmentManager, FRAGMENT_DIALOG_COMP)
        } else {
            SettingsDialog().show(childFragmentManager, FRAGMENT_DIALOG_SETTINGS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == CODE_REQUEST_CAMERA_PERMISSION) {
            if (grantResults.size != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(childFragmentManager, FRAGMENT_DIALOG_ERROR)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}