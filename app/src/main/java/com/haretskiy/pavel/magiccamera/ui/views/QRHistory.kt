package com.haretskiy.pavel.magiccamera.ui.views

import android.widget.TextView

interface QRHistory {
    fun onLongClickHistoryItem(content: String)
    fun onClickHistoryItem(content: String, date: String, contentView: TextView, dateView: TextView)
}