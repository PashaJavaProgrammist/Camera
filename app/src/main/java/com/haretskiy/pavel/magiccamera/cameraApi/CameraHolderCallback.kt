package com.haretskiy.pavel.magiccamera.cameraApi

import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.view.Surface
import android.view.SurfaceHolder
import android.view.WindowManager
import java.io.IOException

class CameraHolderCallback(val windowManager: WindowManager) : SurfaceHolder.Callback {

    var camera: Camera? = null
    var cameraId = -1

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            camera?.setPreviewDisplay(holder)
            camera?.startPreview()
            startFaceDetection()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int,
                                height: Int) {
        camera?.stopPreview()
        setCameraDisplayOrientation(cameraId)
        try {
            camera?.setPreviewDisplay(holder)
            camera?.startPreview()
            startFaceDetection()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.stopFaceDetection()
    }

    private fun setCameraDisplayOrientation(cameraId: Int) {
        // определяем насколько повернут экран от нормального положения
        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result = 0

        // получаем инфо по камере cameraId
        val info = CameraInfo()
        Camera.getCameraInfo(cameraId, info)

        // задняя камера
        if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
            result = 360 - degrees + info.orientation
        } else
        // передняя камера
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                result = 360 - degrees - info.orientation
                result += 360
            }
        result %= 360
        camera?.setDisplayOrientation(result)
    }

    fun startFaceDetection() {
        // Try starting Face Detection
        val params = camera?.parameters
        if (params != null) {
            // start face detection only *after* preview has started
            if (params.maxNumDetectedFaces > 0) {
                // camera supports face detection, so can start it:
                camera?.startFaceDetection()
            }
        }
    }


}