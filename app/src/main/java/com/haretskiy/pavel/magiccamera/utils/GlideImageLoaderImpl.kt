package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader

class GlideImageLoaderImpl(private val context: Context) : ImageLoader {

    override fun loadRoundImageIntoView(imageView: ImageView, uri: String) {
        Glide.with(context)
                .load(uri)
                .apply(RequestOptions
                        .circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
//              .placeholder(R.drawable.loading_spinner)
                .into(imageView)
    }
}