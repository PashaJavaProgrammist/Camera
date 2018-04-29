package com.haretskiy.pavel.magiccamera.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.haretskiy.pavel.magiccamera.storage.Store
import com.haretskiy.pavel.magiccamera.utils.Prefs

class GalleryViewModel(app: Application,
                       store: Store,
                       prefs: Prefs) : AndroidViewModel(app) {

    val allPhotos = LivePagedListBuilder(store.getAllUserPhotos(prefs.getUserEmail()), PagedList.Config.Builder()
            .setPageSize(15)
            .setEnablePlaceholders(true)
            .build()).build()

}