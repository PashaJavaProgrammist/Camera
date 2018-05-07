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
package com.haretskiy.pavel.magiccamera.googleVisionApi.faceDetector

import android.graphics.ImageFormat
import android.util.SparseArray
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import java.nio.ByteBuffer
import java.util.*

/**
 * This is a workaround for a bug in the face detector, in which either very small images (i.e.,
 * most images with dimension < 147) and very thin images can cause a crash in the native face
 * detection code.  This will add padding to such images before face detection in order to avoid
 * this issue.
 *
 *
 *
 * This is not necessary for use with the camera, which doesn't ever create these types of
 * images.
 *
 *
 *
 * This detector should wrap the underlying FaceDetector instance, like this:
 *
 * Detector<Face> safeDetector = new SafeFaceDetector(faceDetector);
 *
 * Replace all remaining occurrences of faceDetector with safeDetector.
</Face> */
class SafeFaceDetector
/**
 * Creates a safe face detector to wrap and protect an underlying face detector from images that
 * trigger the face detector bug.
 */
(private val mDelegate: Detector<Face>) : Detector<Face>() {

    override fun release() {
        mDelegate.release()
    }

    /**
     * Determines whether the supplied image may cause a problem with the underlying face detector.
     * If it does, padding is added to the image in order to avoid the issue.
     */
    override fun detect(frame: Frame): SparseArray<Face> {
        var originalFrame = frame
        val kMinDimension = 147
        val kDimensionLower = 640
        val width = originalFrame.metadata.width
        val height = originalFrame.metadata.height

        if (height > 2 * kDimensionLower) {
            // The image will be scaled down before detection is run.  Check to make sure that this
            // won't result in the width going below the minimum
            val multiple = height.toDouble() / kDimensionLower.toDouble()
            val lowerWidth = Math.floor(width.toDouble() / multiple)
            if (lowerWidth < kMinDimension) {
                // The width would have gone below the minimum when downsampling, so apply padding
                // to the right to keep the width large enough.
                val newWidth = Math.ceil(kMinDimension * multiple).toInt()
                originalFrame = padFrameRight(originalFrame, newWidth)
            }
        } else if (width > 2 * kDimensionLower) {
            // The image will be scaled down before detection is run.  Check to make sure that this
            // won't result in the height going below the minimum
            val multiple = width.toDouble() / kDimensionLower.toDouble()
            val lowerHeight = Math.floor(height.toDouble() / multiple)
            if (lowerHeight < kMinDimension) {
                val newHeight = Math.ceil(kMinDimension * multiple).toInt()
                originalFrame = padFrameBottom(originalFrame, newHeight)
            }
        } else if (width < kMinDimension) {
            originalFrame = padFrameRight(originalFrame, kMinDimension)
        }

        return mDelegate.detect(originalFrame)
    }

    override fun isOperational(): Boolean {
        return mDelegate.isOperational
    }

    override fun setFocus(id: Int): Boolean {
        return mDelegate.setFocus(id)
    }

    /**
     * Creates a new frame based on the original frame, with additional width on the right to
     * increase the size to avoid the bug in the underlying face detector.
     */
    private fun padFrameRight(originalFrame: Frame, newWidth: Int): Frame {
        val metadata = originalFrame.metadata
        val width = metadata.width
        val height = metadata.height

        val origBuffer = originalFrame.grayscaleImageData
        val origOffset = origBuffer.arrayOffset()
        val origBytes = origBuffer.array()

        // This can be changed to just .allocate in the future, when Frame supports non-direct
        // byte buffers.
        val paddedBuffer = ByteBuffer.allocateDirect(newWidth * height)
        val paddedOffset = paddedBuffer.arrayOffset()
        val paddedBytes = paddedBuffer.array()
        Arrays.fill(paddedBytes, 0.toByte())
//        for (int y = 0; y < height; ++y) {
        var y = 0
        while (y < height) {
            val origStride = origOffset + y * width
            val paddedStride = paddedOffset + y * newWidth
            System.arraycopy(origBytes, origStride, paddedBytes, paddedStride, width)
            ++y
        }

        return Frame.Builder()
                .setImageData(paddedBuffer, newWidth, height, ImageFormat.NV21)
                .setId(metadata.id)
                .setRotation(metadata.rotation)
                .setTimestampMillis(metadata.timestampMillis)
                .build()
    }

    /**
     * Creates a new frame based on the original frame, with additional height on the bottom to
     * increase the size to avoid the bug in the underlying face detector.
     */
    private fun padFrameBottom(originalFrame: Frame, newHeight: Int): Frame {
        val metadata = originalFrame.metadata
        val width = metadata.width
        val height = metadata.height

        val origBuffer = originalFrame.grayscaleImageData
        val origOffset = origBuffer.arrayOffset()
        val origBytes = origBuffer.array()

        // This can be changed to just .allocate in the future, when Frame supports non-direct
        // byte buffers.
        val paddedBuffer = ByteBuffer.allocateDirect(width * newHeight)
        val paddedOffset = paddedBuffer.arrayOffset()
        val paddedBytes = paddedBuffer.array()
        Arrays.fill(paddedBytes, 0.toByte())

        // Copy the image content from the original, without bothering to fill in the padded bottom
        // part.
        var y = 0
        while (y < height) {
            val origStride = origOffset + y * width
            val paddedStride = paddedOffset + y * width
            System.arraycopy(origBytes, origStride, paddedBytes, paddedStride, width)
            ++y
        }

        return Frame.Builder()
                .setImageData(paddedBuffer, width, newHeight, ImageFormat.NV21)
                .setId(metadata.id)
                .setRotation(metadata.rotation)
                .setTimestampMillis(metadata.timestampMillis)
                .build()
    }
}
