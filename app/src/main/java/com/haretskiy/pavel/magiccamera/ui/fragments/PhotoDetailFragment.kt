package com.haretskiy.pavel.magiccamera.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.viewModels.PhotoDetailViewModel
import kotlinx.android.synthetic.main.fragment_photo_detail.*
import org.koin.android.ext.android.inject

class PhotoDetailFragment : Fragment() {

    private val photoDetailViewModel: PhotoDetailViewModel by inject()

    private val imageLoader: ImageLoader by inject()

    var uri = EMPTY_STRING
    var date = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = it.getString(BUNDLE_KEY_URI_TO_FRAGMENT_DETAIL)
            date = it.getLong(BUNDLE_KEY_DATE_TO_FRAGMENT_DETAIL, 0L)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_photo_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_delete.setOnClickListener {
            photoDetailViewModel.newDeleteDialogInstance(uri).show(childFragmentManager, FRAGMENT_DIALOG_DELETE)
        }

        iv_share_photo.setOnClickListener {
            photoDetailViewModel.shareImage(uri)
        }

        iv_print_photo.setOnClickListener {
            activity?.let { activity -> photoDetailViewModel.doPhotoPrint(activity, uri) }
        }
    }

    override fun onResume() {
        super.onResume()
        if (uri.isNotEmpty()) imageLoader.loadFullScreenImageIntoViewCenterInside(iv_fullscreen_photo, progress_photo, uri)
        if (date != 0L) tv_date_detail.text = date.convertToDate()
    }

}
