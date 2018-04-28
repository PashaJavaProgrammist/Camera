package com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.graphic

import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.ui.GraphicOverlay


class GraphicTracker<T>(val mOverlay: GraphicOverlay, val mGraphic: TrackedGraphic<T>) : Tracker<T>() {

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    override fun onNewItem(id: Int, item: T?) {
        mGraphic.id = id
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */
    override fun onUpdate(detectionResults: Detector.Detections<T>?, item: T) {
        mOverlay.add(mGraphic)
        mGraphic.updateItem(item)
    }

    /**
     * Hide the graphic when the corresponding face was not detected.  This can happen for
     * intermediate frames temporarily, for example if the face was momentarily blocked from
     * view.
     */
    override fun onMissing(detectionResults: Detector.Detections<T>?) {
        mOverlay.remove(mGraphic)
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    override fun onDone() {
        mOverlay.remove(mGraphic)
    }

}