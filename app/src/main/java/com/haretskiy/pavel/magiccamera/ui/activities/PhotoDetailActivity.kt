package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeletePhotoDialog
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import kotlinx.android.synthetic.main.activity_photo_detail.*
import org.koin.android.ext.android.inject

class PhotoDetailActivity : AppCompatActivity() {

    private val imageLoader: ImageLoader by inject()

    private var uri = EMPTY_STRING
    private var date = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        uri = intent.getStringExtra(BUNDLE_KEY_URI_TO_DETAIL)
        date = intent.getLongExtra(BUNDLE_KEY_DATA_TO_DETAIL, 0L)

        iv_delete.setOnClickListener({
            newDeleteDialogInstance(uri).show(supportFragmentManager, FRAGMENT_DIALOG_DELETE)
        })
    }

    override fun onResume() {
        super.onResume()
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (uri.isNotEmpty()) imageLoader.loadFullScreenImageIntoViewCenterInside(iv_fullscreen_photo, progress_photo, uri)
        if (date != 0L) tv_date_detail.text = date.convertToDate()
    }

    override fun onPause() {
        super.onPause()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun newDeleteDialogInstance(uri: String): DeletePhotoDialog {
        val args = Bundle()
        args.putString(BUNDLE_DIALOG_DELETE_URI, uri)
        args.putBoolean(BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL, true)
        val deleteDialog = DeletePhotoDialog()
        deleteDialog.arguments = args
        return deleteDialog
    }
}
