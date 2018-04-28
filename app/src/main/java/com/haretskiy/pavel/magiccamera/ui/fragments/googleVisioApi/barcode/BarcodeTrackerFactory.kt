package com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.barcode

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.barcode.Barcode
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.graphic.GraphicTracker
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.ui.GraphicOverlay

class BarcodeTrackerFactory(private val mGraphicOverlay: GraphicOverlay) : MultiProcessor.Factory<Barcode> {

    override fun create(barcode: Barcode?) = GraphicTracker(mGraphicOverlay, BarcodeGraphic(mGraphicOverlay))

}