package com.haretskiy.pavel.magiccamera.googleVisionApi.barcodeDerector

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.haretskiy.pavel.magiccamera.googleVisionApi.graphic.GraphicTracker
import com.haretskiy.pavel.magiccamera.googleVisionApi.views.GraphicOverlay

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
internal class BarcodeTrackerFactory(private val mGraphicOverlay: GraphicOverlay) : MultiProcessor.Factory<Barcode> {

    override fun create(barcode: Barcode) = GraphicTracker(mGraphicOverlay, BarcodeGraphic(mGraphicOverlay))
}