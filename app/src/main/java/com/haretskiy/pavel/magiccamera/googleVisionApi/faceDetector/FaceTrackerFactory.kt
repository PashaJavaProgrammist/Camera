package com.haretskiy.pavel.magiccamera.googleVisionApi.faceDetector

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.Face
import com.haretskiy.pavel.magiccamera.googleVisionApi.graphic.GraphicTracker
import com.haretskiy.pavel.magiccamera.googleVisionApi.views.GraphicOverlay

/**
 * Factory for creating a tracker and associated graphic to be associated with a new face.  The
 * multi-processor uses this factory to create face trackers as needed -- one for each individual.
 */
internal class FaceTrackerFactory(private val mGraphicOverlay: GraphicOverlay) : MultiProcessor.Factory<Face> {

    override fun create(face: Face) = GraphicTracker(mGraphicOverlay, FaceGraphic(mGraphicOverlay))

}