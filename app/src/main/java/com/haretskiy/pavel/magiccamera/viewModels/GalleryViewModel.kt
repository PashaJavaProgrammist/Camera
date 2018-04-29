package com.haretskiy.pavel.magiccamera.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.haretskiy.pavel.magiccamera.PAGE_SIZE
import com.haretskiy.pavel.magiccamera.PAGE_SIZE_HINT
import com.haretskiy.pavel.magiccamera.PREFETCH_DISTANCE
import com.haretskiy.pavel.magiccamera.storage.Store
import com.haretskiy.pavel.magiccamera.utils.Prefs

class GalleryViewModel(app: Application,
                       private val store: Store,
                       private val prefs: Prefs) : AndroidViewModel(app) {

    fun getAllUserPhotosLiveData() = LivePagedListBuilder(
            store.getAllUserPhotos(prefs.getUserEmail()),
            PagedList.Config.Builder()
                    .setPageSize(PAGE_SIZE)
                    .setInitialLoadSizeHint(PAGE_SIZE_HINT)
                    .setPrefetchDistance(PREFETCH_DISTANCE)
                    .setEnablePlaceholders(false)
                    .build())
            .build()

}