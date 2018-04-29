package com.haretskiy.pavel.magiccamera.googleVisionApi.googleVisionUtlis

import android.arch.lifecycle.MutableLiveData
import android.content.Context
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
import com.haretskiy.pavel.magiccamera.googleVisionApi.ui.GraphicOverlay
import com.haretskiy.pavel.magiccamera.utils.Toaster

class CameraSourceManager(private val context: Context, private val toaster: Toaster) {

    val cameraSourceLiveData: MutableLiveData<CameraSource> = MutableLiveData()

    fun createCameraSource(faceOverlay: GraphicOverlay, cameraType: Int, cameras: Int) {
        Thread({
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
                toaster.showToast(context.getString(R.string.detector_not_avail), false)
            }
            /* Creates and starts the camera.  Note that this uses a higher resolution in comparison
            * to other detection examples to enable the barcode detector to detect small barcodes
            * at long distances.*/
            when (cameras) {
                NO_CAMERA -> {
                    toaster.showToast(context.getString(R.string.camera_not_found), true)
                }
                else -> {
                    val mCameraSource = CameraSource.Builder(context, multiDetector)
                            .setAutoFocusEnabled(true)
                            .setFacing(cameraType)
                            .setRequestedPreviewSize(MAX_PREVIEW_HEIGHT, MAX_PREVIEW_WIDTH)
                            .setRequestedFps(CAMERA_FPS)
                            .build()
                    cameraSourceLiveData.postValue(mCameraSource)
                }
            }
        }).start()
    }

}