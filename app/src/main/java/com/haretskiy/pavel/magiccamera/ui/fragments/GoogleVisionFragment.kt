package com.haretskiy.pavel.magiccamera.ui.fragments

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.hardware.Camera
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.googleVisionApi.googleVisionUtils.CameraSourceManager
import com.haretskiy.pavel.magiccamera.ui.dialogs.LocationDialog
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionCameraDialog
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionLocationDialog
import com.haretskiy.pavel.magiccamera.utils.LocationService
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import kotlinx.android.synthetic.main.fragment_google_vision.*
import kotlinx.android.synthetic.main.item_frame.*
import org.koin.android.ext.android.inject
import java.io.IOException

class GoogleVisionFragment : Fragment() {

    private val permissionCameraDialog: PermissionCameraDialog by inject()
    private val permissionLocationDialog: PermissionLocationDialog by inject()
    private val toaster: Toaster by inject()
    private val googleApiAvailability: GoogleApiAvailability  by inject()
    private val imageSaver: ImageSaver by inject()
    private val prefs: Prefs by inject()
    private val imageLoader: ImageLoader by inject()
    private val cameraSourceManager: CameraSourceManager by inject()
    private val router: Router by inject()
    private val answers: Answers by inject()

    private var cameraType = CAMERA_TYPE_NOT_FOUND
    private var cameras = Camera.getNumberOfCameras()
    private var mCameraSource: CameraSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraType = prefs.getCameraType()
        cameraSourceManager.cameraSourceLiveData.observe(this, Observer {
            if (mCameraSource != null) {
                mCameraSource?.release()
                mCameraSource = null
            }
            mCameraSource = it
            startCameraSource()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_google_vision, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewsVisible(false)
        bt_change_camera_type.setOnClickListener({ changeCamera() })
        bt_take_a_picture.setOnClickListener({ takePicture() })
        imageLoader.loadRoundImageIntoView(last_photo, prefs.getLastPhotoUri(prefs.getUserEmail()))
        last_photo.setOnClickListener {
            val uri = prefs.getLastPhotoUri(prefs.getUserEmail())
            if (uri.isNotEmpty()) router.startPhotoDetailActivity(uri, 0)
        }

        qr_scanner_switch.setOnClickListener {
            changeQrDetectorState()
            getCameraSourceWithNewDetectorsStates()
        }
        setQrViewsVisibility()
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val permission = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            getCameraSource()
        }
        imageLoader.loadRoundImageIntoView(last_photo, prefs.getLastPhotoUri(prefs.getUserEmail()))
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preview.stop()
        setViewsVisible(false)
        prefs.saveCameraType(cameraType)
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    override fun onDestroy() {
        super.onDestroy()
        try {
            if (mCameraSource != null) {
                mCameraSource?.release()
            }
        } catch (ex: Exception) {
            toaster.showToast("${ex.message}", false)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var i = 0
        while (i < permissions.size) {
            if (permissions[i] == Manifest.permission.CAMERA && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                getCameraSource()
            }
            if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                LocationDialog().show(childFragmentManager, LOCATION_DIALOG)
            }
            ++i
        }
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private fun requestCameraPermission() {
        permissionCameraDialog.show(childFragmentManager, FRAGMENT_DIALOG_COMP)
    }

    private fun requestLocationPermission() {
        permissionLocationDialog.show(childFragmentManager, DIALOG_LOCATION_PERM)
    }

    private fun getCameraSource() {
        initCameraType()
        cameraSourceManager.createCameraSource(faceOverlay, cameraType, cameras)
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {
        setViewsVisible(true)
        // check that the device has play services available.
        val code = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = googleApiAvailability.getErrorDialog(activity, code, RC_HANDLE_GMS)
            dlg.show()
        }
        val cameraSource = mCameraSource
        if (cameraSource != null) {
            try {
                preview.start(cameraSource, faceOverlay)
            } catch (e: IOException) {
                toaster.showToast(getString(R.string.unable_to_start_camera), false)
                mCameraSource?.release()
                mCameraSource = null
            }
        }
    }

    private fun initCameraType() {
        if (cameraType == CAMERA_TYPE_NOT_FOUND) {
            when (cameras) {
                NO_CAMERA -> cameraType = CAMERA_TYPE_NOT_FOUND
                ONE_CAMERA -> cameraType = CameraSource.CAMERA_FACING_BACK
                TWO_CAMERAS -> cameraType = CameraSource.CAMERA_FACING_FRONT
            }
        }
    }

    private fun choseCamera() {
        when (cameras) {
            NO_CAMERA -> cameraType = CAMERA_TYPE_NOT_FOUND
            ONE_CAMERA -> cameraType = CameraSource.CAMERA_FACING_BACK
            TWO_CAMERAS -> cameraType = when (cameraType) {
                CameraSource.CAMERA_FACING_BACK -> CameraSource.CAMERA_FACING_FRONT
                CameraSource.CAMERA_FACING_FRONT -> CameraSource.CAMERA_FACING_BACK
                else -> CameraSource.CAMERA_FACING_FRONT
            }
        }
        answers.logCustom(CustomEvent("Switch camera"))
    }

    private fun changeCamera() {
        if (mCameraSource != null) {
            mCameraSource?.release()
            mCameraSource = null
        }
        choseCamera()
        preview.stop()
        getCameraSource()
    }

    private fun getCameraSourceWithNewDetectorsStates() {
        if (mCameraSource != null) {
            mCameraSource?.release()
            mCameraSource = null
        }
        preview.stop()
        getCameraSource()
    }

    private fun changeQrDetectorState() {
        cameraSourceManager.changeQrDetectorState()
        setQrViewsVisibility()
        cameraSourceManager.qrDetectorNotify()
    }

    private fun setQrViewsVisibility() {
        if (cameraSourceManager.getQrDetectorState()) {
            qr_scanner_switch.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_qr_orange) })
            frame_qr.visibility = View.VISIBLE
        } else {
            qr_scanner_switch.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_qr_white) })
            frame_qr.visibility = View.GONE
        }
    }

    private fun takePicture() {
        try {
            mCameraSource?.takePicture(
                    {
                        makePhotoEffect()
                    },
                    { data ->
                        saveImage(data)
                        requestLocation()
                    })

            answers.logCustom(CustomEvent("Take picture"))
        } catch (ex: Exception) {
            toaster.showToast("${ex.message}", false)
        }
    }

    private fun requestLocation() {
        val permission = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
        if (permission == PackageManager.PERMISSION_GRANTED) {
            LocationDialog().show(childFragmentManager, LOCATION_DIALOG, object : LocationService.LocationResultListener {
                override fun onLocationReceived(location: Location) {
                    answers.logCustom(CustomEvent("Location received" + "Lat: ${location.latitude}, long: ${location.longitude}"))
                    toaster.showToast("Lat: ${location.latitude}, long: ${location.longitude}", false)
                }
            })
        } else {
            requestLocationPermission()
        }
    }

    private fun saveImage(data: ByteArray) {
        imageSaver.saveImage(data)
        Handler().postDelayed({
            imageLoader.loadRoundImageIntoView(last_photo, prefs.getLastPhotoUri(prefs.getUserEmail()))
        }, 200)
    }

    private fun setViewsVisible(doIt: Boolean) {
        if (doIt) {
            last_photo.visibility = View.VISIBLE
            bt_change_camera_type.visibility = View.VISIBLE
            bt_take_a_picture.visibility = View.VISIBLE
        } else {
            last_photo.visibility = View.GONE
            bt_change_camera_type.visibility = View.GONE
            bt_take_a_picture.visibility = View.GONE
        }
    }

    private fun makePhotoEffect() {
        preview.visibility = View.GONE
        setViewsVisible(false)
        Handler().postDelayed({
            preview.visibility = View.VISIBLE
            setViewsVisible(true)
        }, 20)
    }

}
