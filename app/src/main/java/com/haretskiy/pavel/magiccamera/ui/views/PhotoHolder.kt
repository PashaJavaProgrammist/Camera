package com.haretskiy.pavel.magiccamera.ui.views

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.convertToDate
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.viewModels.GalleryViewModel
import kotlinx.android.synthetic.main.item_galery_photo.view.*

class PhotoHolder(
        private val cardView: CardView,
        private val imageLoader: ImageLoader,
        private val photoGallery: PhotoGallery) :
        RecyclerView.ViewHolder(cardView) {

    var uri = EMPTY_STRING

    fun bindHolder(photo: Photo) {
        uri = photo.uri
        cardView.tv_date.text = photo.date.convertToDate()
        imageLoader.loadImageInGallery(cardView.iv_photo_item, cardView.pb_gallery, uri)

        if (photoGallery.isPhotoCheckedToShare(uri)) {
            cardView.share_check.visibility = View.VISIBLE
        } else {
            cardView.share_check.visibility = View.GONE
        }

        cardView.iv_photo_item.setOnClickListener({
            photoGallery.onClickPhoto(uri, photo.date)
        })

        cardView.iv_photo_item.setOnLongClickListener({
            photoGallery.onLongClickPhoto(uri, object : GalleryViewModel.OnCheckedListener {
                override fun onChecked() {
                    cardView.share_check.visibility = View.VISIBLE
                }

                override fun onUnchecked() {
                    cardView.share_check.visibility = View.GONE
                }
            })
        })
    }

}