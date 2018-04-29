package com.haretskiy.pavel.magiccamera.adapters

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.models.ImageModel
import com.haretskiy.pavel.magiccamera.ui.views.ImageModelHolder

class GalleryPhotoAdapter(private val diffUtilCallback: DiffUtil.ItemCallback<ImageModel>)
    : PagedListAdapter<ImageModel, ImageModelHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageModelHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ImageModelHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}