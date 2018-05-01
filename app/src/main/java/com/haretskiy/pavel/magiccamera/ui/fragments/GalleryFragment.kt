package com.haretskiy.pavel.magiccamera.ui.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_URI
import com.haretskiy.pavel.magiccamera.FRAGMENT_DIALOG_DELETE
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.GalleryPhotoAdapter
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeletePhotoDialog
import com.haretskiy.pavel.magiccamera.ui.views.PhotoGallery
import com.haretskiy.pavel.magiccamera.utils.AutoFitGridLayoutManager
import com.haretskiy.pavel.magiccamera.utils.DiffCallBack
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import com.haretskiy.pavel.magiccamera.viewModels.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.android.ext.android.inject

class GalleryFragment : Fragment(), PhotoGallery {

    private val galleryViewModel: GalleryViewModel by inject()
    private val diffCallBack: DiffCallBack by inject()
    private val imageLoader: ImageLoader by inject()
    private val imageSaver: ImageSaver by inject()
    private val router: Router by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_gallery, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c = context
        if (c != null) {
            rcv_gallery_list.layoutManager = AutoFitGridLayoutManager(
                    c,
                    3,
                    resources.getDimension(R.dimen.card_width).toInt(),
                    GridLayoutManager.VERTICAL, false)
        }
        val galleryAdapter = GalleryPhotoAdapter(diffCallBack, imageLoader, this)
        rcv_gallery_list.adapter = galleryAdapter

        galleryViewModel.getAllUserPhotosLiveData().observe(this, Observer { galleryAdapter.submitList(it) })
    }

    override fun onClickPhoto(uri: String, date: Long) {
        router.startPhotoDetailActivity(uri, date)
    }

    override fun onLongClickPhoto(uri: String): Boolean {
        newDeleteDialogInstance(uri).show(childFragmentManager, FRAGMENT_DIALOG_DELETE)
        return true
    }

    private fun newDeleteDialogInstance(uri: String): DeletePhotoDialog {
        val args = Bundle()
        args.putString(BUNDLE_DIALOG_DELETE_URI, uri)
        args.putBoolean(BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL, false)
        val deleteDialog = DeletePhotoDialog()
        deleteDialog.arguments = args
        return deleteDialog
    }

}
