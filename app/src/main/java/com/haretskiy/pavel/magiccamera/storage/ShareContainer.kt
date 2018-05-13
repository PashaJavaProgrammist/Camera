package com.haretskiy.pavel.magiccamera.storage

import com.haretskiy.pavel.magiccamera.models.Photo

interface ShareContainer {
    fun addItem(uri: String)
    fun removeItem(uri: String)
    fun isContains(uri: String): Boolean
    fun clearContainer()
    fun isAtLeastOnePhotoSelected(): Boolean
    fun getCountOfItems(): Int
    fun getAllUris(): ArrayList<String>
    fun selectAll(listOfPhotos: List<Photo>)
}