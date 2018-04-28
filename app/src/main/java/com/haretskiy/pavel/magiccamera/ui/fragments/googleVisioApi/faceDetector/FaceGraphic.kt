package com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.faceDetector

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.vision.face.Face
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.graphic.TrackedGraphic
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.ui.GraphicOverlay

class FaceGraphic(overlay: GraphicOverlay) : TrackedGraphic<Face>(overlay) {

    private val FACE_POSITION_RADIUS = 10.0f
    private val ID_TEXT_SIZE = 40.0f
    private val ID_Y_OFFSET = 50.0f
    private val ID_X_OFFSET = -50.0f
    private val BOX_STROKE_WIDTH = 5.0f

    private val COLOR_CHOICES = intArrayOf(Color.MAGENTA, Color.RED, Color.YELLOW)

    private val mFacePositionPaint = Paint()
    private val mIdPaint: Paint = Paint()
    private val mBoxPaint: Paint = Paint()

    private val mCurrentColorIndex = (0 + 1) % COLOR_CHOICES.size
    val selectedColor = COLOR_CHOICES[mCurrentColorIndex]

    private var mFace: Face? = null

    fun setPaints() {
        mFacePositionPaint.color = selectedColor

        mIdPaint.color = selectedColor
        mIdPaint.textSize = ID_TEXT_SIZE

        mBoxPaint.color = selectedColor
        mBoxPaint.style = Paint.Style.STROKE
        mBoxPaint.strokeWidth = BOX_STROKE_WIDTH
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    override fun updateItem(item: Face) {
        mFace = item
        postInvalidate()
    }

    /**
     * Draws the face annotations for position, size, and ID on the supplied canvas.
     */
    override fun draw(canvas: Canvas) {
        val face = mFace ?: return
        setPaints()
        // Draws a circle at the position of the detected face, with the face's track id below.
        val cx = translateX(face.position.x + face.width / 2)
        val cy = translateY(face.position.y + face.height / 2)
        canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mFacePositionPaint)
        canvas.drawText("id: $id", cx + ID_X_OFFSET, cy + ID_Y_OFFSET, mIdPaint)

        // Draws an oval around the face.
        val xOffset = scaleX(face.width / 2.0f)
        val yOffset = scaleY(face.height / 2.0f)
        val left = cx - xOffset
        val top = cy - yOffset
        val right = cx + xOffset
        val bottom = cy + yOffset
        canvas.drawRect(left, top, right, bottom, mBoxPaint) //oval???
    }

}