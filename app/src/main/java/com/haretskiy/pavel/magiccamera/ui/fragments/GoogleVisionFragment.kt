package com.haretskiy.pavel.magiccamera.ui.fragments

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.googleVisionApi.googleVisionUtlis.CameraSourceManager
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionDialog
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import kotlinx.android.synthetic.main.fragment_google_vision.*
import org.koin.android.ext.android.inject
import java.io.IOException

class GoogleVisionFragment : Fragment() {

    private val permissionDialog: PermissionDialog by inject()
    private val toaster: Toaster by inject()
    private val googleApiAvailability: GoogleApiAvailability  by inject()
    private val imageSaver: ImageSaver by inject()
    private val prefs: Prefs by inject()
    private val imageLoader: ImageLoader by inject()
    private val cameraSourceManager: CameraSourceManager by inject()
    private val router: Router by inject()

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
        last_photo.setOnClickListener({
            val uri = prefs.getLastPhotoUri(prefs.getUserEmail())
            if (uri.isNotEmpty()) router.startPhotoDetailActivity(uri)
        })
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
        if (permissions[0] == Manifest.permission.CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCameraSource()
        }
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private fun requestCameraPermission() {
        permissionDialog.show(childFragmentManager, FRAGMENT_DIALOG_COMP)
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

    private fun takePicture() {
        try {
            mCameraSource?.takePicture(
                    {
                        preview.visibility = View.GONE
                        setViewsVisible(false)
                        Handler().postDelayed({
                            preview.visibility = View.VISIBLE
                            setViewsVisible(true)
                        }, 20)
                    },
                    { data ->
                        imageSaver.saveImage(data)
                        Handler().postDelayed({ imageLoader.loadRoundImageIntoView(last_photo, prefs.getLastPhotoUri(prefs.getUserEmail())) }, 200)
                    })
        } catch (ex: Exception) {
            toaster.showToast("${ex.message}", false)
        }
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

}
