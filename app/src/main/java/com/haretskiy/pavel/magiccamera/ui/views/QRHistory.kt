package com.haretskiy.pavel.magiccamera.ui.views

interface QRHistory {
    fun onLongClickHistoryItem(content: String)
    fun onClickHistoryItem(content: String, date: String)
}