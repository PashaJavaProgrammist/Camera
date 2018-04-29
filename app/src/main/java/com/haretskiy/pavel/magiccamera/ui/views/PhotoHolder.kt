package com.haretskiy.pavel.magiccamera.ui.views

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import com.haretskiy.pavel.magiccamera.convertToDate
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import kotlinx.android.synthetic.main.item_galery_photo.view.*

class PhotoHolder(private val cardView: CardView, private val imageLoader: ImageLoader, private val router: Router) :
        RecyclerView.ViewHolder(cardView) {

    fun bindHolder(photo: Photo) {
        cardView.tv_date.text = photo.date.convertToDate()
        imageLoader.loadImageInGallery(cardView.iv_photo_item, cardView.pb_gallery, photo.uri)
        cardView.iv_photo_item.setOnClickListener({ onClickImage(photo.uri, photo.date) })
    }

    private fun onClickImage(uri: String, date: Long) {
        router.startPhotoDetailActivity(uri, date)
    }

}