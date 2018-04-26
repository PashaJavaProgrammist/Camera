package com.haretskiy.pavel.magiccamera.ui.fragments.cameraApi

import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.RectF
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.*
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_CAMERA1_ID
import com.haretskiy.pavel.magiccamera.FULL_SCREEN
import com.haretskiy.pavel.magiccamera.R
import kotlinx.android.synthetic.main.fragment_camera.*
import org.koin.android.ext.android.inject

class CameraFragment : Fragment() {

    private val holderCallback: HolderCallback by inject()
    private var cameras = 0

    private var holder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var currentCameraID = -1
    private val backgroundHndler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA)
        cameras = Camera.getNumberOfCameras()
        setCameraId(savedInstanceState?.getInt(BUNDLE_KEY_CAMERA1_ID, 0) ?: 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHolder()

        change_cameras.setOnClickListener({ changeCamera() })
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        openCamera()
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        closeCamera()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BUNDLE_KEY_CAMERA1_ID, currentCameraID)
    }

    private fun closeCamera() {
        if (camera != null)
            camera?.release()
        camera = null
        holderCallback.camera = null
    }

    private fun openCamera() {
        if (currentCameraID == -1) {
            setCameraId(0)
        }
        camera = Camera.open(currentCameraID)
        holderCallback.camera = camera
        setPreviewSize(FULL_SCREEN)
    }

    private fun initHolder() {
        holder = surfaceView.holder
        holder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        holder?.addCallback(holderCallback)
    }

    private fun setPreviewSize(fullScreen: Boolean) {

        // получаем размеры экрана
        val display = holderCallback.windowManager.defaultDisplay
        val widthIsMax = display.width > display.height

        // определяем размеры превью камеры
        val size = camera?.parameters?.previewSize

        val rectDisplay = RectF()
        val rectPreview = RectF()

        // RectF экрана, соотвествует размерам экрана
        rectDisplay.set(0f, 0f, display.width.toFloat(), display.height.toFloat())
        if (size != null) {
            // RectF первью
            if (widthIsMax) {
                // превью в горизонтальной ориентации
                rectPreview.set(0f, 0f, size.width.toFloat(), size.height.toFloat())
            } else {
                // превью в вертикальной ориентации
                rectPreview.set(0f, 0f, size.height.toFloat(), size.width.toFloat())
            }
        }
        val matrix = Matrix()
        // подготовка матрицы преобразования
        if (!fullScreen) {
            // если превью будет "втиснут" в экран (второй вариант из урока)
            matrix.setRectToRect(rectPreview, rectDisplay,
                    Matrix.ScaleToFit.START)
        } else {
            // если экран будет "втиснут" в превью (третий вариант из урока)
            matrix.setRectToRect(rectDisplay, rectPreview,
                    Matrix.ScaleToFit.START)
            matrix.invert(matrix)
        }
        // преобразование
        matrix.mapRect(rectPreview)

        // установка размеров surface из получившегося преобразования
        surfaceView.layoutParams.height = rectPreview.bottom.toInt()
        surfaceView.layoutParams.width = rectPreview.right.toInt()
    }

    private fun choseCamera() {
        if (cameras == 1) {
            setCameraId(0)
        } else if (cameras == 2) {
            when (currentCameraID) {
                -1 -> setCameraId(0)
                0 -> setCameraId(1)
                else -> setCameraId(0)
            }
        }
    }

    private fun changeCamera() {
        backgroundHndler.post({
            surfaceView.visibility = View.GONE
            closeCamera()
            choseCamera()
            openCamera()
            surfaceView.visibility = View.VISIBLE
        })
    }

    private fun setCameraId(id: Int) {
        currentCameraID = id
        holderCallback.cameraId = id
    }

}
