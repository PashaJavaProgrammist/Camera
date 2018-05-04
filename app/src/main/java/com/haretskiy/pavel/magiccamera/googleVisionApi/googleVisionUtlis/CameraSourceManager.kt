package com.haretskiy.pavel.magiccamera.googleVisionApi.googleVisionUtlis

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.os.Handler
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiDetector
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.googleVisionApi.barcodeDerector.BarcodeGraphic
import com.haretskiy.pavel.magiccamera.googleVisionApi.barcodeDerector.BarcodeTrackerFactory
import com.haretskiy.pavel.magiccamera.googleVisionApi.faceDetector.FaceTrackerFactory
import com.haretskiy.pavel.magiccamera.googleVisionApi.views.GraphicOverlay
import com.haretskiy.pavel.magiccamera.models.BarCode
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class CameraSourceManager(
        private val context: Context,
        private val toaster: Toaster,
        private val barCodeStore: BarCodeStore,
        private val prefs: Prefs,
        private val router: Router) {

    private var timeOfLastResult = 0L

    val cameraSourceLiveData: MutableLiveData<CameraSource> = MutableLiveData()

    fun createCameraSource(faceOverlay: GraphicOverlay, cameraType: Int, cameras: Int) {
        Handler().post({

            val multiDetectorBuilder = MultiDetector.Builder()

            val qrDetectorState = prefs.getQRDetectorState()

            val faceDetector = FaceDetector.Builder(context)
                    .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                    .setTrackingEnabled(true)
                    .build()
            val faceFactory = FaceTrackerFactory(faceOverlay)
            faceDetector.setProcessor(MultiProcessor.Builder<Face>(faceFactory).build())

            multiDetectorBuilder.add(faceDetector)

            if (qrDetectorState) {
                val barcodeDetector = BarcodeDetector.Builder(context).build()
                val barcodeFactory = BarcodeTrackerFactory(faceOverlay)
                barcodeFactory.barcodeGraphic.addBarcodeScannerListener(object : BarcodeGraphic.BarcodeScannerListener {
                    override fun onCodeFounded(resultScanning: String) {
                        val time = System.currentTimeMillis()
                        if (time - timeOfLastResult > BARCODE_SCAN_DELAY) {
                            router.startBarcodeActivity(resultScanning)
                            timeOfLastResult = time
                            try {
                                barCodeStore.insert(BarCode(resultScanning, prefs.getUserEmail(), System.currentTimeMillis()))
                            } catch (ex: Exception) {
                                toaster.showToast(context.getString(R.string.unable_save_res_scan) + "${ex.message}", false)
                            }
                        }
                    }
                })
                barcodeDetector.setProcessor(MultiProcessor.Builder<Barcode>(barcodeFactory).build())

                multiDetectorBuilder.add(barcodeDetector)
            }

            val multiDetector = multiDetectorBuilder.build()

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
        })
    }

    fun changeQrDetectorState() {
        prefs.turnOnQRDetector(!prefs.getQRDetectorState())
    }

    fun getQrDetectorState() = prefs.getQRDetectorState()

    fun qrDetectorNotify() {
        if (prefs.getQRDetectorState()) toaster.showToast(context.getString(R.string.qr_on), false)
        else toaster.showToast(context.getString(R.string.qr_off), false)
    }
}