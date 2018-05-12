package com.haretskiy.pavel.magiccamera.storage

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

    override fun isItemChecked() = urisToMultiShare.size > 0

    override fun getCountOfItems(): Int = urisToMultiShare.size

    override fun getAllUris() = urisToMultiShare
}