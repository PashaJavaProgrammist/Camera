package com.haretskiy.pavel.magiccamera.googleVisionApi.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.vision.CameraSource
import com.haretskiy.pavel.magiccamera.googleVisionApi.graphic.Graphic
import java.util.*

/**
 * A view which renders a series of custom graphics to be overlayed on top of an associated preview
 * (i.e., the camera preview).  The creator can add graphics objects, update the objects, and remove
 * them, triggering the appropriate drawing and invalidation within the view.
 *
 *
 *
 * Supports scaling and mirroring of the graphics relative the camera's preview properties.  The
 * idea is that detection items are expressed in terms of a preview size, but need to be scaled up
 * to the full view size, and also mirrored in the case of the front-facing camera.
 *
 *
 *
 * Associated [Graphic] items should use the following methods to convert to view coordinates
 * for the graphics that are drawn:
 *
 *  1. [Graphic.scaleX] and [Graphic.scaleY] adjust the size of the
 * supplied value from the preview scale to the view scale.
 *  1. [Graphic.translateX] and [Graphic.translateY] adjust the coordinate
 * from the preview's coordinate system to the view coordinate system.
 *
 */
class GraphicOverlay(context: Context, attrs: AttributeSet) : View(context, attrs) {
    //    private val mLock = Any()
    private var mPreviewWidth: Int = 0
    var mWidthScaleFactor = 1.0f
    private var mPreviewHeight: Int = 0
    var mHeightScaleFactor = 1.0f
    var mFacing = CameraSource.CAMERA_FACING_BACK
    private val mGraphics = HashSet<Graphic>()


    /**
     * Removes all graphics from the overlay.
     */
    fun clear() {
//        synchronized(mLock) {
        mGraphics.clear()
//        }
        postInvalidate()
    }

    /**
     * Adds a graphic to the overlay.
     */
    fun add(graphic: Graphic) {
//        synchronized(mLock) {
        mGraphics.add(graphic)
//        }
        postInvalidate()
    }

    /**
     * Removes a graphic from the overlay.
     */
    fun remove(graphic: Graphic) {
//        synchronized(mLock) {
        mGraphics.remove(graphic)
//        }
        postInvalidate()
    }

    /**
     * Sets the camera attributes for size and facing direction, which informs how to transform
     * image coordinates later.
     */
    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
//        synchronized(mLock) {
        mPreviewWidth = previewWidth
        mPreviewHeight = previewHeight
        mFacing = facing
//        }
        postInvalidate()
    }

    /**
     * Draws the overlay with its associated graphic objects.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        synchronized(mLock) {
        if (mPreviewWidth != 0 && mPreviewHeight != 0) {
            mWidthScaleFactor = canvas.width.toFloat() / mPreviewWidth.toFloat()
            mHeightScaleFactor = canvas.height.toFloat() / mPreviewHeight.toFloat()
        }

        for (graphic in mGraphics) {
            graphic.draw(canvas)
        }
//        }
    }
}
