package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.QrHistoryAdapter
import com.haretskiy.pavel.magiccamera.ui.views.QRHistory
import com.haretskiy.pavel.magiccamera.viewModels.QrHistoryVewModel
import kotlinx.android.synthetic.main.fragment_qrhistory.*
import org.koin.android.ext.android.inject

class QrHistoryFragment : Fragment(), QRHistory {

    private val qrHistoryVewModel: QrHistoryVewModel by inject()

    private val adapter by lazy {
        context?.let { QrHistoryAdapter(it, this, emptyList()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        qrHistoryVewModel.storageBarCodesLiveData.observe(this, Observer {
            if (it != null) adapter?.list = it
            adapter?.notifyDataSetChanged()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_qrhistory, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_qr_history.layoutManager = LinearLayoutManager(context)
        rv_qr_history.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    override fun onClickHistoryItem(content: String) {
        qrHistoryVewModel.startBarcodeActivity(content)
    }

    override fun onLongClickHistoryItem(content: String) {
        qrHistoryVewModel.deleteQrCodeFromDB(childFragmentManager, content)
    }
}
