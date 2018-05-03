package com.haretskiy.pavel.magiccamera.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.models.BarCode
import com.haretskiy.pavel.magiccamera.ui.views.QRHistory
import com.haretskiy.pavel.magiccamera.ui.views.QrHistoryHolder

class QrHistoryAdapter(val context: Context, private val qrHistory: QRHistory, var list: List<BarCode>) : RecyclerView.Adapter<QrHistoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QrHistoryHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_qr_history, parent, false)
        return QrHistoryHolder(qrHistory, view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: QrHistoryHolder, position: Int) {
        holder.bindHolder(list[position])
    }

}