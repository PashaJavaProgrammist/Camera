package com.haretskiy.pavel.magiccamera.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.graphics.RectF
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.cameraApi.CameraHolderCallback
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionDialog
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import org.koin.android.ext.android.inject

class CameraFragment : Fragment() {

    private val holderCallback: CameraHolderCallback by inject()
    private val imageSaverImpl: ImageSaver by inject()
    private val permissionDialog: PermissionDialog by inject()
    private val prefs: Prefs by inject()

    private var cameras = 0
    private var currentCameraID = -1

    private var camera: Camera? = null
    private var holder: SurfaceHolder? = null
    private var drawHolder: SurfaceHolder? = null
    private val backgroundHndler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA)
        cameras = Camera.getNumberOfCameras()
        setCameraId(prefs.getCameraApi1Id())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        // Inflate the layout for this fragment
        view.drawSurface.setZOrderOnTop(true)
//        drawSurface.setZOrderMediaOverlay(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHolder()

        change_cameras.setOnClickListener({ changeCamera() })
        make_picture.setOnClickListener({ takePicture() })
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
        prefs.saveCameraApi1Id(currentCameraID)
    }

    private fun closeCamera() {
        if (camera != null) {
            camera?.release()
            camera = null
            holderCallback.camera = null
        }
    }

    private fun openCamera() {
        val permission = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            if (currentCameraID == -1) {
                setCameraId(0)
            }
            camera = Camera.open(currentCameraID)
            holderCallback.camera = camera
            setPreviewSize(FULL_SCREEN)
        }
    }


    private fun initHolder() {
        holder = surfaceView.holder.apply {
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
            addCallback(holderCallback)
        }

        drawHolder = drawSurface.holder.apply {
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
            setFormat(PixelFormat.RGBA_8888)
        }
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
        Matrix().apply {
            // подготовка матрицы преобразования
            if (!fullScreen) {
                // если превью будет "втиснут" в экран (второй вариант из урока)
                setRectToRect(rectPreview, rectDisplay, Matrix.ScaleToFit.START)
            } else {
                // если экран будет "втиснут" в превью (третий вариант из урока)
                setRectToRect(rectDisplay, rectPreview, Matrix.ScaleToFit.START)
                invert(this)
            }
            // преобразование
            mapRect(rectPreview)
        }
        // установка размеров surface из получившегося преобразования
        surfaceView.layoutParams.height = rectPreview.bottom.toInt()
        surfaceView.layoutParams.width = rectPreview.right.toInt()
    }

    private fun choseCamera() {
        if (cameras == ONE_CAMERA) {
            setCameraId(0)
        } else if (cameras == TWO_CAMERAS) {
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

    private fun takePicture() {
        camera?.takePicture(null, null, Camera.PictureCallback { data, _ ->
            imageSaverImpl.saveImage(data)
            surfaceView.visibility = View.GONE
            surfaceView.visibility = View.VISIBLE
        })
    }

    private fun requestCameraPermission() {
        permissionDialog.show(childFragmentManager, FRAGMENT_DIALOG_COMP)
    }


}
