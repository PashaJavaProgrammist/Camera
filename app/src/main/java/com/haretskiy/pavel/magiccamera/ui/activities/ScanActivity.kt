package com.haretskiy.pavel.magiccamera.ui.activities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.FaceDetector
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_URI_TO_ACTIVITY_SCAN
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.googleVisionApi.faceDetector.SafeFaceDetector
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {

    var uri = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        uri = intent.getStringExtra(BUNDLE_KEY_URI_TO_ACTIVITY_SCAN)
        scanPhoto(uri)
    }

    private fun scanPhoto(uri: String) {
        val bitmap = BitmapFactory.decodeFile(uri)
        val detector = FaceDetector.Builder(this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build()
        val safeDetector = SafeFaceDetector(detector)
        val frame = Frame.Builder().setBitmap(bitmap).build()
        val faces = safeDetector.detect(frame)

        faceView.setContent(bitmap, faces)

        Toast.makeText(this, "${faces.size()} faces", Toast.LENGTH_SHORT).show()
        safeDetector.release()
    }
}
