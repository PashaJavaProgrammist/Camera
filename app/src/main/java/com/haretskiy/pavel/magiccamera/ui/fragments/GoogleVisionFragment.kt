package com.haretskiy.pavel.magiccamera.ui.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiDetector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.haretskiy.pavel.magiccamera.FRAGMENT_DIALOG_COMP
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.RC_HANDLE_GMS
import com.haretskiy.pavel.magiccamera.googleVisioApi.barcodeDerector.BarcodeTrackerFactory
import com.haretskiy.pavel.magiccamera.googleVisioApi.faceDetector.FaceTrackerFactory
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionDialog
import com.haretskiy.pavel.magiccamera.utils.Toaster
import kotlinx.android.synthetic.main.fragment_qr.*
import org.koin.android.ext.android.inject
import java.io.IOException

class GoogleVisionFragment : Fragment() {

    private val permissionDialog: PermissionDialog by inject()
    private val toaster: Toaster by inject()
    private val barcodeDetector: BarcodeDetector by inject()
    private val barcodeFactory: BarcodeTrackerFactory by inject()
    private val faceDetector: FaceDetector by inject()
    private val faceFactory: FaceTrackerFactory by inject()
    private val googleApiAvailability: GoogleApiAvailability  by inject()

    private var mCameraSource: CameraSource? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val permission = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            createCameraSource()
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

        faceDetector.setProcessor(MultiProcessor.Builder<Face>(faceFactory.initialize(faceOverlay)).build())
        barcodeDetector.setProcessor(MultiProcessor.Builder<Barcode>(barcodeFactory.initialize(faceOverlay)).build())

        val multiDetector = MultiDetector.Builder()
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

        /* Creates and starts the camera.  Note that this uses a higher resolution in comparison
        * to other detection examples to enable the barcode detector to detect small barcodes
        * at long distances.*/
        mCameraSource = CameraSource.Builder(context, multiDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f)
                .build()
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        preview.stop()
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
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private fun startCameraSource() {

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
