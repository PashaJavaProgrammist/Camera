package com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.graphic

import com.haretskiy.pavel.magiccamera.ui.fragments.googleVisioApi.ui.GraphicOverlay

abstract class TrackedGraphic<T>(overlay: GraphicOverlay) : Graphic(overlay) {

    var id: Int = 0

    abstract fun updateItem(item: T)
}