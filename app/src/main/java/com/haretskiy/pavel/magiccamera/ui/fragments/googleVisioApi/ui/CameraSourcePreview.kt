package com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import java.io.IOException


class CameraSourcePreview @JvmOverloads constructor(mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(mContext, attrs, defStyleAttr) {

    private val TAG = "CameraSourcePreview"
    var mStartRequested = false
    var mSurfaceAvailable = false
    private var mSurfaceView = SurfaceView(mContext)
    private var mCameraSource: CameraSource? = null

    private var mOverlay: GraphicOverlay? = null

    private fun setSurfaceParams() {
        mSurfaceView.holder.addCallback(SurfaceCallback())
        addView(mSurfaceView)
    }

    @Throws(IOException::class)
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

    @Throws(IOException::class)
    fun start(cameraSource: CameraSource, overlay: GraphicOverlay) {
        removeAllViews()
        setSurfaceParams()
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

    private fun isPortraitMode(): Boolean {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true
        }

        Log.d(TAG, "isPortraitMode returning false by default")
        return false
    }

    @Throws(IOException::class)
    private fun startIfReady() {
        val permission = context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //not perm
        } else {
            if (mStartRequested && mSurfaceAvailable) {
                mCameraSource?.start(mSurfaceView.holder)
                val size = mCameraSource?.previewSize
                if (mOverlay != null && size != null) {
                    val min = Math.min(size.width, size.height)
                    val max = Math.max(size.width, size.height)
                    if (isPortraitMode()) {
                        // Swap width and height sizes when in portrait, since it will be rotated by
                        // 90 degrees
                        mOverlay?.setCameraInfo(min, max, mCameraSource?.cameraFacing ?: 0)
                    } else {
                        mOverlay?.setCameraInfo(max, min, mCameraSource?.cameraFacing ?: 0)
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
                Log.e(TAG, "Could not start camera source.", e)
            }

        }

        override fun surfaceDestroyed(surface: SurfaceHolder) {
            mSurfaceAvailable = false
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var width = 320
        var height = 240
        if (mCameraSource != null) {
            val size = mCameraSource?.previewSize
            if (size != null) {
                width = size.width
                height = size.height
            }
        }

        // Swap width and height sizes when in portrait, since it will be rotated 90 degrees
        if (isPortraitMode()) {
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
        if (childHeight > layoutHeight) {
            childHeight = layoutHeight
            childWidth = (layoutHeight.toFloat() / height.toFloat() * width).toInt()
        }

        for (i in 0 until childCount) {
            getChildAt(i).layout(0, 0, childWidth, childHeight)
        }

        try {
            startIfReady()
        } catch (e: IOException) {
            Log.e(TAG, "Could not start camera source.", e)
        }

    }

}