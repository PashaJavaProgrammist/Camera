package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_BARCODE_RESULT
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import kotlinx.android.synthetic.main.activity_barcode_scan_result.*
import org.koin.android.ext.android.inject

class BarcodeScanResultActivity : AppCompatActivity() {

    private val router: Router by inject()
    private var scanResult = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scan_result)

        scanResult = intent?.getStringExtra(BUNDLE_KEY_BARCODE_RESULT) ?: EMPTY_STRING
        if (scanResult.isNotEmpty()) tv_result_scan.text = scanResult

        bt_share.setOnClickListener { if (scanResult.isNotEmpty()) router.shareText(scanResult) }

        bt_open_in_app.setOnClickListener {
            router.openCustomTabs(scanResult)
        }
    }
}
