package com.haretskiy.pavel.magiccamera.googleVisioApi.graphic

import android.graphics.Canvas
import com.google.android.gms.vision.CameraSource
import com.haretskiy.pavel.magiccamera.googleVisioApi.ui.GraphicOverlay


/**
 * Base class for a custom graphics object to be rendered within the graphic overlay.  Subclass
 * this and implement the [Graphic.draw] method to define the
 * graphics element.  Add instances to the overlay using [GraphicOverlay.add].
 */
abstract class Graphic(private val mOverlay: GraphicOverlay) {

    /**
     * Draw the graphic on the supplied canvas.  Drawing should use the following methods to
     * convert to view coordinates for the graphics that are drawn:
     *
     *  1. [Graphic.scaleX] and [Graphic.scaleY] adjust the size of
     * the supplied value from the preview scale to the view scale.
     *  1. [Graphic.translateX] and [Graphic.translateY] adjust the
     * coordinate from the preview's coordinate system to the view coordinate system.
     *
     *
     * @param canvas drawing canvas
     */
    abstract fun draw(canvas: Canvas)

    /**
     * Adjusts a horizontal value of the supplied value from the preview scale to the view
     * scale.
     */
    fun scaleX(horizontal: Float): Float {
        return horizontal * mOverlay.mWidthScaleFactor
    }

    /**
     * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
     */
    fun scaleY(vertical: Float): Float {
        return vertical * mOverlay.mHeightScaleFactor
    }

    /**
     * Adjusts the x coordinate from the preview's coordinate system to the view coordinate
     * system.
     */
    fun translateX(x: Float): Float {
        return if (mOverlay.mFacing == CameraSource.CAMERA_FACING_FRONT) {
            mOverlay.width - scaleX(x)
        } else {
            scaleX(x)
        }
    }

    /**
     * Adjusts the y coordinate from the preview's coordinate system to the view coordinate
     * system.
     */
    fun translateY(y: Float): Float {
        return scaleY(y)
    }

    fun postInvalidate() {
        mOverlay.postInvalidate()
    }
}
