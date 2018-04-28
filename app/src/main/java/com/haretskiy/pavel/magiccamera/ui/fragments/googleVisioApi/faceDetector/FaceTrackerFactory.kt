package com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.faceDetector

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.Face
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.graphic.GraphicTracker
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.ui.GraphicOverlay

class FaceTrackerFactory(private val mGraphicOverlay: GraphicOverlay) : MultiProcessor.Factory<Face> {

    override fun create(face: Face) = GraphicTracker(mGraphicOverlay, FaceGraphic(mGraphicOverlay))
}