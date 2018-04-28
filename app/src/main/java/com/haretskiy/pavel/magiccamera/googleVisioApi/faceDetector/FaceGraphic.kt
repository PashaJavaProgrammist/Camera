package com.haretskiy.pavel.magiccamera.googleVisioApi.faceDetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import com.google.android.gms.vision.face.Face
import com.haretskiy.pavel.magiccamera.BOX_STROKE_WIDTH
import com.haretskiy.pavel.magiccamera.ID_TEXT_SIZE
import com.haretskiy.pavel.magiccamera.googleVisioApi.graphic.TrackedGraphic
import com.haretskiy.pavel.magiccamera.googleVisioApi.ui.GraphicOverlay

class FaceGraphic(overlay: GraphicOverlay) : TrackedGraphic<Face>(overlay) {

    private val mFacePositionPaint: Paint
    private val mIdPaint: Paint
    private val mBoxPaint: Paint

    private var mFace: Face? = null

    init {

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.size
        val selectedColor = COLOR_CHOICES[mCurrentColorIndex]

        mFacePositionPaint = Paint()
        mFacePositionPaint.color = selectedColor

        mIdPaint = Paint()
        mIdPaint.color = selectedColor
        mIdPaint.textSize = ID_TEXT_SIZE

        mBoxPaint = Paint()
        mBoxPaint.color = selectedColor
        mBoxPaint.style = Paint.Style.STROKE
        mBoxPaint.strokeWidth = BOX_STROKE_WIDTH
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    override fun updateItem(item: Face?) {
        mFace = item
        postInvalidate()
    }

    /**
     * Draws the face annotations for position, size, and ID on the supplied canvas.
     */
    override fun draw(canvas: Canvas) {
        val face = mFace ?: return

        // Draws a circle at the position of the detected face, with the face's track id below.
        val cx = translateX(face.position.x + face.width / 2)
        val cy = translateY(face.position.y + face.height / 2)
//        canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mFacePositionPaint)
//        canvas.drawText("id: $id", cx + ID_X_OFFSET, cy + ID_Y_OFFSET, mIdPaint)

        // Draws an oval around the face.
        val xOffset = scaleX(face.width / 2.0f)
        val yOffset = scaleY(face.height / 2.0f)
        val left = cx - xOffset
        val top = cy - yOffset
        val right = cx + xOffset
        val bottom = cy + yOffset
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawOval(left, top, right, bottom, mBoxPaint)
        } else {
            canvas.drawRect(left, top, right, bottom, mBoxPaint)
        }
    }

    companion object {
        private val COLOR_CHOICES = intArrayOf(Color.MAGENTA, Color.RED, Color.YELLOW)
        private var mCurrentColorIndex = 0
    }
}