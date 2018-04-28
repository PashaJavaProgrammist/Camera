package com.haretskiy.pavel.magiccamera.googleVisionApi.graphic

import com.haretskiy.pavel.magiccamera.googleVisionApi.ui.GraphicOverlay

/**
 * Common base class for defining graphics for a particular item type.  This along with
 * [GraphicTracker] avoids the need to duplicate this code for both the face and barcode
 * instances.
 */
abstract class TrackedGraphic<T>(overlay: GraphicOverlay) : Graphic(overlay) {
    var id: Int = 0

    abstract fun updateItem(item: T?)
}