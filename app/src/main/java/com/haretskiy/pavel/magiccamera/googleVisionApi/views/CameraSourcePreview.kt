package com.haretskiy.pavel.magiccamera.googleVisionApi.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import java.io.IOException

class CameraSourcePreview(private val mContext: Context, attrs: AttributeSet) : ViewGroup(mContext, attrs) {
    private val mSurfaceView: SurfaceView
    private var mStartRequested: Boolean = false
    private var mSurfaceAvailable: Boolean = false
    private var mCameraSource: CameraSource? = null

    private var mOverlay: GraphicOverlay? = null

    private val isPortraitMode: Boolean
        get() {
            val orientation = mContext.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return false
            }
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                return true
            }
            return false
        }

    init {
        mStartRequested = false
        mSurfaceAvailable = false

        mSurfaceView = SurfaceView(mContext)
        mSurfaceView.holder.addCallback(SurfaceCallback())
        addView(mSurfaceView)
    }

    fun start(cameraSource: CameraSource?) {
        if (cameraSource == null) {
            stop()
        }

        mCameraSource = cameraSource

        if (mCameraSource != null) {
            mStartRequested = true
            startIfReady()
        }
    }

    fun start(cameraSource: CameraSource, overlay: GraphicOverlay) {
        mOverlay = overlay
        start(cameraSource)
    }

    fun stop() {
        if (mCameraSource != null) {
            mCameraSource?.stop()
        }
    }

    fun release() {
        if (mCameraSource != null) {
            mCameraSource?.release()
            mCameraSource = null
        }
    }

    private fun startIfReady() {
        if (mStartRequested && mSurfaceAvailable) {
            val permission = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
            if (permission == PackageManager.PERMISSION_GRANTED) {
                mCameraSource?.start(mSurfaceView.holder)
                if (mOverlay != null) {
                    val size = mCameraSource?.previewSize
                    if (size != null) {
                        val min = Math.min(size.width, size.height)
                        val max = Math.max(size.width, size.height)

                        if (isPortraitMode) {
                            // Swap width and height sizes when in portrait, since it will be rotated by
                            // 90 degrees
                            mOverlay?.setCameraInfo(min, max, mCameraSource?.cameraFacing ?: 0)
                        } else {
                            mOverlay?.setCameraInfo(max, min, mCameraSource?.cameraFacing ?: 0)
                        }
                    }
                    mOverlay?.clear()
                }
                mStartRequested = false
            }
        }
    }

    private inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(surface: SurfaceHolder) {
            mSurfaceAvailable = true
            try {
                startIfReady()
            } catch (e: IOException) {
                //Could not start camera source
            }

        }

        override fun surfaceDestroyed(surface: SurfaceHolder) {
            mSurfaceAvailable = false
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var width = 2560
        var height = 1440
        if (mCameraSource != null) {
            val size = mCameraSource?.previewSize
            if (size != null) {
                width = size.width
                height = size.height
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode) {
            val tmp = width
            width = height
            height = tmp
        }

        val layoutWidth = right - left
        val layoutHeight = bottom - top

        // Computes height and width for potentially doing fit width.
        var childWidth = layoutWidth
        var childHeight = (layoutWidth.toFloat() / width.toFloat() * height).toInt()

        // If height is too tall using fit width, does fit height instead.
        if (childHeight < layoutHeight) {
            childHeight = layoutHeight
            childWidth = (layoutHeight.toFloat() / height.toFloat() * width).toInt()
        }


        var i = 0
        while (i != childCount) {
            getChildAt(i).layout(0, 0, childWidth, childHeight)
            ++i
        }

        try {
            startIfReady()
        } catch (e: IOException) {
            //Could not start camera source
        }

    }
}