package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.*
import android.support.v4.content.ContextCompat.getColor
import android.support.v4.content.FileProvider
import android.support.v4.util.Pair
import android.view.View
import android.widget.TextView
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.ShareEvent
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.ui.activities.*
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import java.io.File
import android.util.Pair as UtilPair

class RouterImpl(private val context: Context,
                 private val answers: Answers) : Router {

    override fun startHostActivity() {
        val intent = Intent(context, HostActivity::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun startLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun startSettingsActivity() {
        val openSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("$PACKAGE_SETTINGS${context.packageName}"))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            openSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(openSettingsIntent)
    }

    override fun doFragmentTransaction(fragment: Fragment, fragmentManager: FragmentManager, containerId: Int) {
        val ft = fragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
        ft.commit()
    }

    override fun doFragmentTransaction(fragment: android.app.Fragment, fragmentManager: android.app.FragmentManager?, containerId: Int) {
        if (fragmentManager != null) {
            val ft = fragmentManager.beginTransaction().apply {
                replace(containerId, fragment)
            }
            ft.commit()
        }
    }

    override fun startPhotoDetailActivity(uri: String, date: Long) {
        val intent = Intent(context, PhotoDetailActivity::class.java)
        intent.putExtra(BUNDLE_KEY_URI_TO_ACTIVITY_DETAIL, uri)
        intent.putExtra(BUNDLE_KEY_DATA_TO_DETAIL, date)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        answers.logContentView(ContentViewEvent()
                .putContentName("photo")
                .putContentType("image"))
        context.startActivity(intent)
    }

    override fun startBarcodeActivity(resultOfScanning: String, date: String) {
        val intent = Intent(context, QrScanResultActivity::class.java)
        intent.putExtra(BUNDLE_KEY_BARCODE_RESULT, resultOfScanning)
        intent.putExtra(BUNDLE_KEY_BARCODE_DATE, date)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        answers.logContentView(ContentViewEvent()
                .putContentName("qr")
                .putContentType("qr_string"))
        context.startActivity(intent)
    }

    override fun startBarcodeActivityWithAnimation(context: Context, activity: FragmentActivity, content: String,
                                                   date: String, contentView: TextView, dateView: TextView) {
        val intent = Intent(context, QrScanResultActivity::class.java)
        intent.putExtra(BUNDLE_KEY_BARCODE_RESULT, content)
        intent.putExtra(BUNDLE_KEY_BARCODE_DATE, date)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        answers.logContentView(ContentViewEvent()
                .putContentName("qr")
                .putContentType("qr_string"))

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                Pair<View, String>(contentView, VIEW_NAME_CONTENT),
                Pair<View, String>(dateView, VIEW_NAME_DATE))

        ActivityCompat.startActivity(context, intent, activityOptions.toBundle())
    }

    override fun shareText(resultOfScanning: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, resultOfScanning)
        sendIntent.type = SHARE_TYPE_TEXT
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        answers.logShare(ShareEvent()
                .putMethod("shareQr")
                .putContentName("qr")
                .putContentType("string"))

        context.startActivity(sendIntent)
    }

    override fun shareImage(imageUri: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = SHARE_TYPE_IMAGE
        val uri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, File(imageUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        answers.logShare(ShareEvent()
                .putMethod("shareImage")
                .putContentName("imageUri")
                .putContentType("image"))

        context.startActivity(shareIntent)
    }

    override fun shareImages(uris: ArrayList<String>) {
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        shareIntent.type = SHARE_TYPE_IMAGE

        val imageUris = uris.map { FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, File(it)) } as java.util.ArrayList

        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        answers.logShare(ShareEvent()
                .putMethod("share ${uris.size} images")
                .putContentName("imageUri")
                .putContentType("image"))

        context.startActivity(shareIntent)
    }

    override fun openCustomTabs(uri: String) {
        val builder = CustomTabsIntent.Builder().apply {
            setToolbarColor(getColor(context, R.color.colorPrimary))
            setSecondaryToolbarColor(getColor(context, R.color.colorAccent))
            setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            setExitAnimations(context, android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        val customTabsIntent = builder.build()
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(context, Uri.parse(uri))

        answers.logContentView(ContentViewEvent()
                .putContentName("uri")
                .putContentType("uri"))
    }

    override fun startScanningActivity(uri: String) {
        val intent = Intent(context, ScanActivity::class.java)
        intent.putExtra(BUNDLE_KEY_URI_TO_ACTIVITY_SCAN, uri)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        answers.logContentView(ContentViewEvent()
                .putContentName("photo_scan")
                .putContentType("image"))
        context.startActivity(intent)
    }

    override fun startLocationSettingsActivity() {
        val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun startMapActivity(latitude: Double, longitude: Double) {
        val intent = Intent(context, MapsActivity::class.java)
        intent.putExtra(BUNDLE_KEY_LATITUDE, latitude)
        intent.putExtra(BUNDLE_KEY_LONGITUDE, longitude)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}