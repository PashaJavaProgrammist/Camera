package com.haretskiy.pavel.magiccamera.ui.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.adapters.GalleryPhotoAdapter
import com.haretskiy.pavel.magiccamera.pagging.DiffCallBack
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import com.haretskiy.pavel.magiccamera.viewmodels.GalleryViewModel
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.android.ext.android.inject

class GalleryFragment : Fragment() {

    private val galleryViewModel: GalleryViewModel by inject()
    private val diffCallBack: DiffCallBack by inject()
    private val imageLoader: ImageLoader by inject()
    private val router: Router by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcv_gallery_list.layoutManager = GridLayoutManager(context, 3)
        val galleryAdapter = GalleryPhotoAdapter(diffCallBack, imageLoader, router)
        rcv_gallery_list.adapter = galleryAdapter

        galleryViewModel.allPhotos.observe(this, Observer { galleryAdapter.submitList(it) })
    }

}
