package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.GalleryPhotoAdapter
import com.haretskiy.pavel.magiccamera.ui.activities.HostActivity
import com.haretskiy.pavel.magiccamera.ui.views.PhotoGallery
import com.haretskiy.pavel.magiccamera.utils.AutoFitGridLayoutManager
import com.haretskiy.pavel.magiccamera.utils.DiffCallBack
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.viewModels.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.android.ext.android.inject

class GalleryFragment : Fragment(), PhotoGallery {

    private val galleryViewModel: GalleryViewModel by inject()
    private val diffCallBack: DiffCallBack by inject()
    private val imageLoader: ImageLoader by inject()

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

        rcv_gallery_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (fab_gallery != null)
                    if (dy > 0 && fab_gallery.visibility == View.VISIBLE) {
                        fab_gallery.hide()
                    } else if (dy < 0 && fab_gallery.visibility != View.VISIBLE) {
                        fab_gallery.show()
                    }
            }
        })

        fab_gallery.setOnClickListener {
            galleryViewModel.turnOffQrDetector()
            (activity as HostActivity).selectItemCamera()
        }
    }

    override fun onClickPhoto(uri: String, date: Long) {
        galleryViewModel.runDetailActivity(uri, date)
    }

    override fun onLongClickPhoto(uri: String): Boolean {
        galleryViewModel.deletePhoto(childFragmentManager, uri)
        return true
    }

}
