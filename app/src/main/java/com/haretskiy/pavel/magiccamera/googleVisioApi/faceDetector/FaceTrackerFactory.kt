package com.haretskiy.pavel.magiccamera.googleVisioApi.faceDetector

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face
import com.haretskiy.pavel.magiccamera.googleVisioApi.graphic.GraphicTracker
import com.haretskiy.pavel.magiccamera.googleVisioApi.ui.GraphicOverlay

/**
 * Factory for creating a tracker and associated graphic to be associated with a new face.  The
 * multi-processor uses this factory to create face trackers as needed -- one for each individual.
 */
internal class FaceTrackerFactory(private val mGraphicOverlay: GraphicOverlay) : MultiProcessor.Factory<Face> {

    override fun create(face: Face): Tracker<Face> {
        val graphic = FaceGraphic(mGraphicOverlay)
        return GraphicTracker(mGraphicOverlay, graphic)
    }
}