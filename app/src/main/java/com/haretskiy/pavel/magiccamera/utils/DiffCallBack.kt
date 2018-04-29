package com.haretskiy.pavel.magiccamera.utils

import android.support.v7.util.DiffUtil
import com.haretskiy.pavel.magiccamera.models.Photo

class DiffCallBack : DiffUtil.ItemCallback<Photo>() {

    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.uri == newItem.uri
}
