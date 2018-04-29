package com.haretskiy.pavel.magiccamera.adapters

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.ui.views.PhotoHolder
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class GalleryPhotoAdapter(diffUtilCallback: DiffUtil.ItemCallback<Photo>,
                          private val imageLoader: ImageLoader,
                          private val router: Router)
    : PagedListAdapter<Photo, PhotoHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_galery_photo, parent, false) as CardView
        return PhotoHolder(view, imageLoader, router)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) holder.bindHolder(photo)
    }
}