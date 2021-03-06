package com.haretskiy.pavel.magiccamera.ui.views

import android.support.v7.widget.RecyclerView
import android.view.View
import com.haretskiy.pavel.magiccamera.convertToDate
import com.haretskiy.pavel.magiccamera.models.BarCode
import kotlinx.android.synthetic.main.item_qr_history.view.*

class QrHistoryHolder(val qrHistory: QRHistory, val view: View) : RecyclerView.ViewHolder(view) {

    lateinit var barCode: BarCode

    fun bindHolder(barCode: BarCode) {
        this.barCode = barCode
        view.tv_content_history.text = barCode.code
        view.tv_date_qr_history.text = barCode.date.convertToDate()

        view.setOnClickListener { qrHistory.onClickHistoryItem(barCode.code, barCode.date.convertToDate(), view.tv_content_history, view.tv_date_qr_history) }
        view.setOnLongClickListener {
            qrHistory.onLongClickHistoryItem(barCode.code)
            true
        }
    }

}