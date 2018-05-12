package com.haretskiy.pavel.magiccamera.storage

interface ShareContainer {
    fun addItem(uri: String)
    fun removeItem(uri: String)
    fun isContains(uri: String): Boolean
    fun clearContainer()
    fun isItemChecked(): Boolean
    fun getCountOfItems(): Int
    fun getAllUris(): ArrayList<String>
}