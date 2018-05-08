package com.haretskiy.pavel.magiccamera.ui.fragments

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.FRAGMENT_DIALOG_COMP
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.TAG
import com.haretskiy.pavel.magiccamera.camera2Api.Camera2Helper
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionDialog
import com.haretskiy.pavel.magiccamera.utils.Prefs
import kotlinx.android.synthetic.main.fragment_camera2.*
import org.koin.android.ext.android.inject
import java.util.*
import java.util.concurrent.TimeUnit

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class Camera2Fragment : Fragment(), View.OnClickListener {

    private val cameraManager: CameraManager by inject()
    private val permissionDialog: PermissionDialog by inject()
    private val prefs: Prefs by inject()
    private val camera2Helper: Camera2Helper by inject()

    private var isAfterResumeFlag = false

    /**
     * [TextureView.SurfaceTextureListener] handles several lifecycle events on a [TextureView].
     */
    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera(camera2Helper.currentCameraID, width, height)
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
            camera2Helper.configureTransform(width, height)
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture) = true

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        camera2Helper.getAvailableCameras()
        camera2Helper.currentCameraID = prefs.getCameraId()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_camera2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bt_take_picture.setOnClickListener(this)
        bt_change_camera.setOnClickListener(this)
        bt_take_picture.visibility = View.GONE
        bt_change_camera.visibility = View.GONE
        spinner_sizes.visibility = View.GONE
        initSpinnerListener()
        initSpinnerAdapter(camera2Helper.sizesOfScreen)
    }

    /** When the screen is turned off and turned back on, the SurfaceTexture is already
    available, and "onSurfaceTextureAvailable" will not be called. In that case, we can openCamera
    a camera and start preview from here (otherwise, we wait until the surface is ready in
    the SurfaceTextureListener).*/
    override fun onResume() {
        super.onResume()
        isAfterResumeFlag = true
        camera2Helper.texture = texture
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        camera2Helper.startBackgroundThread()
        choseCamera(false)
        getAvailableSizes(camera2Helper.currentCameraID)
        openCamera()
    }

    override fun onPause() {
        prefs.saveCameraId(camera2Helper.currentCameraID)
        closeCamera()
        camera2Helper.stopBackgroundThread()
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_take_picture -> camera2Helper.takePicture()
            R.id.bt_change_camera -> changeCamera()
        }
    }

    private fun initSpinnerAdapter(array: Array<Size>) {
        spinner_sizes.adapter = null
        spinner_sizes.adapter = ArrayAdapter(context, R.layout.item_border_text_string, array)
    }

    private fun initSpinnerListener() {

        spinner_sizes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
//                closeCamera()
//                currentSizeOfScreen = Collections.max(Arrays.asList(*sizesOfScreen), comparatorAreas)
//                openCamera()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                closeCamera()
                if (!isAfterResumeFlag) {
                    camera2Helper.currentSizeOfScreen = camera2Helper.sizesOfScreen[position]
                    prefs.saveCameraScreenSize(camera2Helper.currentCameraID, position)
                }
                isAfterResumeFlag = false
                openCamera()
            }
        }
    }

    private fun choseCamera(byButton: Boolean) {
        when (byButton) {
            true -> choseCamera()
            false -> if (camera2Helper.currentCameraID == EMPTY_STRING) choseCamera()
        }
    }

    private fun choseCamera() {
        if (camera2Helper.cameraIdList.size == 1) {
            camera2Helper.currentCameraID = camera2Helper.cameraIdList[0]
        } else if (camera2Helper.cameraIdList.size == 2) {
            camera2Helper.currentCameraID = when (camera2Helper.currentCameraID) {
                EMPTY_STRING -> camera2Helper.cameraIdList[0]
                camera2Helper.cameraIdList[0] -> camera2Helper.cameraIdList[1]
                else -> camera2Helper.cameraIdList[0]
            }
        }
    }

    private fun changeCamera() {
        closeCamera()
        choseCamera(true)
        getAvailableSizes(camera2Helper.currentCameraID)
        openCamera()
    }

    private fun openCamera() {
        if (texture.isAvailable) {
            openCamera(camera2Helper.currentCameraID, texture.width, texture.height)
        } else {
            texture.surfaceTextureListener = surfaceTextureListener
        }
    }

    /**
     * Opens the camera specified by [Camera2Helper.currentCameraID].
     */
    private fun openCamera(cameraId: String, width: Int, height: Int) {
        val permission = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        } else {
            camera2Helper.setUpCameraOutputs(cameraId, width, height)
            camera2Helper.configureTransform(width, height)
            try {
                // Wait for camera to openCamera - 2.5 seconds is sufficient
                if (!camera2Helper.cameraOpenCloseLock.tryAcquire(5000, TimeUnit.MILLISECONDS)) {
                    throw RuntimeException("Time out waiting to lock camera opening.")
                }
                cameraManager.openCamera(cameraId, camera2Helper.stateCallback, camera2Helper.backgroundHandler)
            } catch (e: CameraAccessException) {
                Log.e(TAG, e.toString())
            } catch (e: InterruptedException) {
                throw RuntimeException("Interrupted while trying to lock camera opening.", e)
            }
            bt_take_picture.visibility = View.VISIBLE
            bt_change_camera.visibility = View.VISIBLE
            spinner_sizes.visibility = View.VISIBLE
        }
    }

    /**
     * Closes the current [CameraDevice].
     */
    private fun closeCamera() {
        try {
            camera2Helper.cameraOpenCloseLock.acquire()
            camera2Helper.captureSession?.close()
            camera2Helper.captureSession = null
            camera2Helper.cameraDevice?.close()
            camera2Helper.cameraDevice = null
            camera2Helper.imageReader?.close()
            camera2Helper.imageReader = null
            bt_take_picture.visibility = View.GONE
            bt_change_camera.visibility = View.GONE
            spinner_sizes.visibility = View.GONE
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            camera2Helper.cameraOpenCloseLock.release()
        }
    }

    private fun getAvailableSizes(id: String) {
        try {
            val characteristics = cameraManager.getCameraCharacteristics(id)
            camera2Helper.configurationMap = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: return
            camera2Helper.sizesOfScreen = camera2Helper.configurationMap.getOutputSizes(ImageFormat.JPEG)
            Arrays.sort(camera2Helper.sizesOfScreen, camera2Helper.comparatorAreas)
            val pos = prefs.getCameraScreenSizePosition(camera2Helper.currentCameraID)
            if (pos != -1) {
                camera2Helper.currentSizeOfScreen = camera2Helper.sizesOfScreen[pos]
                Handler().post({ spinner_sizes.setSelection(pos) })
            } else {
                camera2Helper.currentSizeOfScreen = Collections.max(Arrays.asList(*camera2Helper.sizesOfScreen), camera2Helper.comparatorAreas)
            }
            initSpinnerAdapter(camera2Helper.sizesOfScreen)
            /*
            // For still image captures, we use the largest available size.
            currentSizeOfScreen = Collections.max(Arrays.asList(*sizesOfScreen), comparatorAreas)
            */
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: NullPointerException) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            Log.e(TAG, e.toString())
        }
    }

    private fun requestCameraPermission() {
        permissionDialog.show(childFragmentManager, FRAGMENT_DIALOG_COMP)
    }

}