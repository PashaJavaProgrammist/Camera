package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.models.BarCode
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import kotlinx.android.synthetic.main.activity_barcode_scan_result.*
import org.koin.android.ext.android.inject
import java.util.regex.Pattern

class BarcodeScanResultActivity : AppCompatActivity() {

    private val router: Router by inject()
    private var scanResult = EMPTY_STRING
    private val toaster: Toaster by inject()
    private val barCodeStore: BarCodeStore by inject()
    private val prefs: Prefs by inject()

    private val pattern1 = Pattern.compile(URL_REGEX1)
    private val pattern2 = Pattern.compile(URL_REGEX2, Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scan_result)

        scanResult = intent?.getStringExtra(BUNDLE_KEY_BARCODE_RESULT) ?: EMPTY_STRING
        if (scanResult.isNotEmpty()) {
            tv_result_scan.text = scanResult
            try {
                barCodeStore.insert(BarCode(scanResult, prefs.getUserEmail(), System.currentTimeMillis()))
            } catch (ex: Exception) {
                toaster.showToast(getString(R.string.unable_save_res_scan) + "${ex.message}", false)
            }
        }

        bt_share.setOnClickListener { if (scanResult.isNotEmpty()) router.shareText(scanResult) }

        bt_open_in_app.setOnClickListener {
            try {
                val m1 = pattern1.matcher(scanResult)
                val m2 = pattern2.matcher(scanResult)
                if (m1.find() || m2.find()) {
                    router.openCustomTabs(scanResult)
                } else {
                    toaster.showToast(getString(R.string.url_do_not_found), false)
                }
            } catch (ex: Exception) {
                toaster.showToast(getString(R.string.another_browser) + "${ex.message}", true)
            }
        }
    }
}
