package com.haretskiy.pavel.magiccamera.storage

import com.haretskiy.pavel.magiccamera.models.Photo

class ShareContainerImpl : ShareContainer {

    private var urisToMultiShare = ArrayList<String>()

    override fun addItem(uri: String) {
        urisToMultiShare.add(uri)
    }

    override fun removeItem(uri: String) {
        urisToMultiShare.remove(uri)
    }

    override fun isContains(uri: String) = urisToMultiShare.contains(uri)

    override fun clearContainer() {
        urisToMultiShare.clear()
    }

    override fun isAtLeastOnePhotoSelected() = urisToMultiShare.size > 0

    override fun getCountOfItems(): Int = urisToMultiShare.size

    override fun getAllUris() = urisToMultiShare

    override fun selectAll(listOfPhotos: List<Photo>) {
        for (item in listOfPhotos) {
            if (!urisToMultiShare.contains(item.uri)) {
                urisToMultiShare.add(item.uri)
            }
        }
    }
}