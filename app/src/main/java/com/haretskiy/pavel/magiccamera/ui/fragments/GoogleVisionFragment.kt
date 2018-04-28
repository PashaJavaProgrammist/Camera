package com.haretskiy.pavel.magiccamera.ui.fragments

import android.Manifest
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
import com.google.android.gms.vision.MultiDetector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.googleVisionApi.barcodeDerector.BarcodeTrackerFactory
import com.haretskiy.pavel.magiccamera.googleVisionApi.faceDetector.FaceTrackerFactory
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionDialog
import com.haretskiy.pavel.magiccamera.utils.ImageSaver
import com.haretskiy.pavel.magiccamera.utils.Toaster
import kotlinx.android.synthetic.main.fragment_qr.*
import org.koin.android.ext.android.inject
import java.io.IOException

class GoogleVisionFragment : Fragment() {

    private val permissionDialog: PermissionDialog by inject()
    private val toaster: Toaster by inject()
    private val googleApiAvailability: GoogleApiAvailability  by inject()
    private val imageSaver: ImageSaver by inject()

    private var cameraType = NOTHIHG_CAMERA

    private var cameras = Camera.getNumberOfCameras()

    private var mCameraSource: CameraSource? = null

    private lateinit var multiDetector: MultiDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraType = savedInstanceState?.getInt(BUNDLE_KEY_CAMERA_GOOGLE, NOTHIHG_CAMERA)
                ?: NOTHIHG_CAMERA
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonsVisible(false)
        val permission = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            createCameraSource()
        }
        bt_change_camera_type.setOnClickListener({ changeCamera() })
        bt_take_a_picture.setOnClickListener({ takePicture() })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BUNDLE_KEY_CAMERA_GOOGLE, cameraType)
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        startCameraSource()
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        preview.stop()
        setButtonsVisible(false)
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mCameraSource != null) {
            mCameraSource?.release()
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

    private fun createCameraSource() {

        val faceDetector = FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setTrackingEnabled(true)
                .build()
        val faceFactory = FaceTrackerFactory(faceOverlay)
        faceDetector.setProcessor(MultiProcessor.Builder<Face>(faceFactory).build())

        val barcodeDetector = BarcodeDetector.Builder(context).build()
        val barcodeFactory = BarcodeTrackerFactory(faceOverlay)
        barcodeDetector.setProcessor(
                MultiProcessor.Builder<Barcode>(barcodeFactory).build())

        multiDetector = MultiDetector.Builder()
                .add(faceDetector)
                .add(barcodeDetector)
                .build()

        if (!multiDetector.isOperational) {
            /** Note: The first time that an app using the barcode or face API is installed on a
             * device, GMS will download a native libraries to the device in order to do detection.
             * Usually this completes before the app is run for the first time.  But if that
             * download has not yet completed, then the above call will not detect any barcodes
             * and/or faces.
             *
             * isOperational() can be used to check if the required native libraries are currently
             * available.  The detectors will automatically become operational once the library
             * downloads complete on device.*/
            toaster.showToast(getString(R.string.detector_not_avail), false)
        }
        initCameraType()
        /* Creates and starts the camera.  Note that this uses a higher resolution in comparison
        * to other detection examples to enable the barcode detector to detect small barcodes
        * at long distances.*/
        when (cameras) {
            NO_CAMERA -> {
                toaster.showToast(getString(R.string.camera_not_found), true)
            }
            else -> {
                mCameraSource = CameraSource.Builder(context, multiDetector)
                        .setAutoFocusEnabled(true)
                        .setFacing(cameraType)
                        .setRequestedPreviewSize(MAX_PREVIEW_HEIGHT, MAX_PREVIEW_WIDTH)
                        .setRequestedFps(CAMERA_FPS)
                        .build()
            }
        }
    }

    private fun initCameraType() {
        if (cameraType == NOTHIHG_CAMERA) {
            when (cameras) {
                NO_CAMERA -> cameraType = NOTHIHG_CAMERA
                ONE_CAMERA -> cameraType = CameraSource.CAMERA_FACING_BACK
                TWO_CAMERAS -> cameraType = CameraSource.CAMERA_FACING_FRONT
            }
        }
    }

    private fun choseCamera() {
        if (cameras == NO_CAMERA) {
            cameraType = NOTHIHG_CAMERA
        } else if (cameras == ONE_CAMERA) {
            cameraType = CameraSource.CAMERA_FACING_BACK
        } else if (cameras == TWO_CAMERAS) {
            cameraType = when (cameraType) {
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
        createCameraSource()
        startCameraSource()
    }

    private fun takePicture() {
        mCameraSource?.takePicture(
                {
                    preview.visibility = View.GONE
                    Handler().postDelayed({ preview.visibility = View.VISIBLE }, 20)
                },
                { data ->
                    imageSaver.saveImageApi1(data)
                })
    }


    private fun setButtonsVisible(doIt: Boolean) {
        if (doIt) {
            bt_change_camera_type.visibility = View.VISIBLE
            bt_take_a_picture.visibility = View.VISIBLE
        } else {
            bt_change_camera_type.visibility = View.GONE
            bt_take_a_picture.visibility = View.GONE
        }
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {
        setButtonsVisible(true)
        // check that the device has play services available.
        val code = googleApiAvailability.isGooglePlayServicesAvailable(context)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = googleApiAvailability.getErrorDialog(activity, code, RC_HANDLE_GMS)
            dlg.show()
        }

        if (mCameraSource != null) {
            try {
                preview.start(mCameraSource!!, faceOverlay)
            } catch (e: IOException) {
                toaster.showToast(getString(R.string.unable_to_start_camera), false)
                mCameraSource?.release()
                mCameraSource = null
            }
        }
    }

}
