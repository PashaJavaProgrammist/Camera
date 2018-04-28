package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader

class GlideImageLoaderImpl(private val context: Context) : ImageLoader {

    override fun loadRoundImageIntoView(imageView: ImageView, uri: String) {
        Glide.with(context)
                .load(uri)
                .apply(RequestOptions
                        .circleCropTransform()
                        .override(300, 300)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round))
                .into(imageView)
    }
}