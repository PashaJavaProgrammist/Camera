package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.viewModels.QrResultDetailViewModel
import kotlinx.android.synthetic.main.activity_qr_scan_result.*
import org.koin.android.ext.android.inject

class QrScanResultActivity : AppCompatActivity() {

    private var scanResult = EMPTY_STRING
    private var date = EMPTY_STRING

    private val qrResultDetailViewModel: QrResultDetailViewModel by inject()
    private val answers: Answers by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan_result)

        ViewCompat.setTransitionName(tv_content_history, VIEW_NAME_CONTENT)
        ViewCompat.setTransitionName(tv_date_qr_history, VIEW_NAME_DATE)

        scanResult = intent?.getStringExtra(BUNDLE_KEY_BARCODE_RESULT) ?: EMPTY_STRING
        date = intent?.getStringExtra(BUNDLE_KEY_BARCODE_DATE) ?: EMPTY_STRING
        answers.logCustom(CustomEvent(getString(R.string.qr_scanned)).putCustomAttribute(getString(R.string.res), scanResult))

        if (scanResult.isNotEmpty()) {
            tv_content_history.text = scanResult
            tv_date_qr_history.text = date
        }

        bt_share.setOnClickListener { if (scanResult.isNotEmpty()) qrResultDetailViewModel.shareUrl(scanResult) }
        bt_open_in_app.setOnClickListener { qrResultDetailViewModel.openUrlInApp(scanResult) }
        iv_delete_qr.setOnClickListener { qrResultDetailViewModel.onClickDelete(scanResult, supportFragmentManager) }
    }

}
