package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader

class GlideImageLoaderImpl(private val context: Context) : ImageLoader {

    override fun loadRoundImageIntoView(imageView: ImageView, uri: String) {
        Handler().post({
            Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions
                            .circleCropTransform()
                            .override(100, 100)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round))
                    .into(imageView)
        })
    }

    override fun loadFullScreenImageIntoViewCenterInside(imageView: ImageView, progressBar: ProgressBar, uri: String) {
        Handler().post({
            Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions
                            .centerInsideTransform()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(imageView)
        })
    }

    override fun loadFullScreenImageIntoViewCenterCrop(imageView: ImageView, progressBar: ProgressBar, uri: String) {
        Handler().post({
            Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions
                            .centerCropTransform()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(imageView)
        })
    }

    override fun loadImageInGallery(imageView: ImageView, progressBar: ProgressBar, uri: String) {
        Handler().post({
            Glide.with(context)
                    .load(uri)
                    .apply(RequestOptions
                            .centerCropTransform()
                            .override(500, 500)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .into(imageView)
        })
    }
}