package com.haretskiy.pavel.magiccamera.ui.views

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import com.haretskiy.pavel.magiccamera.convertToDate
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import kotlinx.android.synthetic.main.item_galery_photo.view.*

class PhotoHolder(
        private val cardView: CardView,
        private val imageLoader: ImageLoader,
        private val photoGallery: PhotoGallery) :
        RecyclerView.ViewHolder(cardView) {

    fun bindHolder(photo: Photo) {
        cardView.tv_date.text = photo.date.convertToDate()
        imageLoader.loadImageInGallery(cardView.iv_photo_item, cardView.pb_gallery, photo.uri)
        cardView.iv_photo_item.setOnClickListener({
            photoGallery.onClickPhoto(photo.uri, photo.date)
        })
        cardView.iv_photo_item.setOnLongClickListener({
            photoGallery.onLongClickPhoto(photo.uri)
        })
    }


}