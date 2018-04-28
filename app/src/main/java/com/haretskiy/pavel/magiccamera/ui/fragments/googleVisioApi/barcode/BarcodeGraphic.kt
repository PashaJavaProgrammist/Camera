package com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.barcode

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.google.android.gms.vision.barcode.Barcode
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.graphic.TrackedGraphic
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.ui.GraphicOverlay


class BarcodeGraphic(overlay: GraphicOverlay) : TrackedGraphic<Barcode>(overlay) {

    private val COLOR_CHOICES = intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN)
    private val mCurrentColorIndex = (0 + 1) % COLOR_CHOICES.size

    private val mRectPaint = Paint()

    private val mTextPaint = Paint()

    private var mBarcode: Barcode? = null

    val selectedColor = COLOR_CHOICES[mCurrentColorIndex]

    fun setPaints() {
        mRectPaint.color = selectedColor
        mRectPaint.style = Paint.Style.STROKE
        mRectPaint.strokeWidth = 4.0f

        mTextPaint.color = selectedColor
        mTextPaint.textSize = 36.0f
    }

    override fun updateItem(item: Barcode) {
        mBarcode = item
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        val barcode = mBarcode ?: return
        setPaints()
        // Draws the bounding box around the barcode.
        val rect = RectF(barcode.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        canvas.drawRect(rect, mRectPaint)

        // Draws a label at the bottom of the barcode indicate the barcode value that was detected.
        canvas.drawText(barcode.rawValue, rect.left, rect.bottom, mTextPaint)
    }
}