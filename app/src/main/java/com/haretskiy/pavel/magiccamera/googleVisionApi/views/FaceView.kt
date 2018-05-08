/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haretskiy.pavel.magiccamera.googleVisionApi.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import com.google.android.gms.vision.face.Face

/**
 * View which displays a bitmap containing a face along with overlay graphics that identify the
 * locations of detected facial landmarks.
 */
class FaceView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mBitmap: Bitmap? = null
    private var mFaces: SparseArray<Face> = SparseArray()
    private val paint = Paint()

    /**
     * Sets the bitmap background and the associated face detections.
     */
    internal fun setContent(bitmap: Bitmap, faces: SparseArray<Face>) {
        mBitmap = bitmap
        mFaces = faces
        invalidate()
    }

    /**
     * Draws the bitmap background and the associated face landmarks.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mBitmap != null) {
            val scale = drawBitmap(canvas)
            drawFaceAnnotations(canvas, scale)
        }
    }

    /**
     * Draws the bitmap background, scaled to the device size.  Returns the scale for future use in
     * positioning the facial landmark graphics.
     */
    private fun drawBitmap(canvas: Canvas): Double {
        val bitmap = mBitmap
        if (bitmap != null) {
            val viewWidth = canvas.width.toDouble()
            val viewHeight = canvas.height.toDouble()
            val imageWidth = bitmap.width.toDouble()
            val imageHeight = bitmap.height.toDouble()
            val scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight)

            val destBounds = Rect(0, 0, (imageWidth * scale).toInt(), (imageHeight * scale).toInt())
            canvas.drawBitmap(bitmap, null, destBounds, null)
            return scale
        }
        return 1.0
    }

    /**
     * Draws a small circle for each detected landmark, centered at the detected landmark position.
     *
     *
     *
     * Note that eye landmarks are defined to be the midpoint between the detected eye corner
     * positions, which tends to place the eye landmarks at the lower eyelid rather than at the
     * pupil position.
     */
    private fun drawFaceAnnotations(canvas: Canvas, scale: Double) {
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        for (i in 0 until mFaces.size()) {
            val face = mFaces.valueAt(i)

            val rect = RectF(
                    (face.position.x * scale).toFloat(),
                    (face.position.y * scale).toFloat(),
                    (face.position.x + face.width) * scale.toFloat(),
                    (face.position.y + face.height) * scale.toFloat())
            canvas.drawRect(rect, paint)

//            paint.strokeWidth = 2f
//            for (landmark in face.landmarks) {
//                val cx = (landmark.position.x * scale).toInt()
//                val cy = (landmark.position.y * scale).toInt()
//                canvas.drawCircle(cx.toFloat(), cy.toFloat(), 10f, paint)
//            }
        }
    }
}
