package com.haretskiy.pavel.magiccamera.adapters

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.models.ImageModel
import com.haretskiy.pavel.magiccamera.ui.views.ImageModelHolder

class GalleryPhotoAdapter(private val diffUtilCallback: DiffUtil.ItemCallback<ImageModel>)
    : PagedListAdapter<ImageModel, ImageModelHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageModelHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_galery_photo, parent, false)
        return ImageModelHolder(view)
    }

    override fun onBindViewHolder(holder: ImageModelHolder, position: Int) {
        holder.bindHolder(getItem(position))
    }
}