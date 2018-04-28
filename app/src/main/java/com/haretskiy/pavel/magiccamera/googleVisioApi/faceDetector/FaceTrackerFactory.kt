package com.haretskiy.pavel.magiccamera.googleVisioApi.faceDetector

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.Face
import com.haretskiy.pavel.magiccamera.googleVisioApi.graphic.GraphicTracker
import com.haretskiy.pavel.magiccamera.googleVisioApi.ui.GraphicOverlay

/**
 * Factory for creating a tracker and associated graphic to be associated with a new face.  The
 * multi-processor uses this factory to create face trackers as needed -- one for each individual.
 */
internal class FaceTrackerFactory : MultiProcessor.Factory<Face> {

    private lateinit var mGraphicOverlay: GraphicOverlay

    fun initialize(graphicOverlay: GraphicOverlay): FaceTrackerFactory {
        mGraphicOverlay = graphicOverlay
        return this
    }

    override fun create(face: Face) = GraphicTracker(mGraphicOverlay, FaceGraphic(mGraphicOverlay))

}