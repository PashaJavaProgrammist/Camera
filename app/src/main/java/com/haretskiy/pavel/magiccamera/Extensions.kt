package com.haretskiy.pavel.magiccamera

import java.text.SimpleDateFormat
import java.util.*

fun Long.convertToDate(): String = SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).format(Date(this))