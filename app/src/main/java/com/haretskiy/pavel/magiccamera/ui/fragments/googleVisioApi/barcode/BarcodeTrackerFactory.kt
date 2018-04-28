package com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.barcode

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.graphic.GraphicTracker
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.ui.GraphicOverlay

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
internal class BarcodeTrackerFactory(private val mGraphicOverlay: GraphicOverlay) : MultiProcessor.Factory<Barcode> {

    override fun create(barcode: Barcode): Tracker<Barcode> {
        val graphic = BarcodeGraphic(mGraphicOverlay)
        return GraphicTracker(mGraphicOverlay, graphic)
    }
}