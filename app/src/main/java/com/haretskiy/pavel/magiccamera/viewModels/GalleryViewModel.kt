package com.haretskiy.pavel.magiccamera.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeletePhotoDialog
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class GalleryViewModel(app: Application,
                       private val photoStore: PhotoStore,
                       private val prefs: Prefs,
                       private val router: Router) : AndroidViewModel(app) {

    fun getAllUserPhotosLiveData() = LivePagedListBuilder(
            photoStore.getAllUserPhotosDataSourceFactory(prefs.getUserEmail()),
            PagedList.Config.Builder()
                    .setPageSize(PAGE_SIZE)
                    .setInitialLoadSizeHint(PAGE_SIZE_HINT)
                    .setPrefetchDistance(PREFETCH_DISTANCE)
                    .setEnablePlaceholders(false)
                    .build())
            .build()

    fun deletePhoto(fm: FragmentManager, uri: String) {
        newDeleteDialogInstance(uri).show(fm, FRAGMENT_DIALOG_DELETE)
    }

    private fun newDeleteDialogInstance(uri: String): DeletePhotoDialog {
        val args = Bundle()
        args.putString(BUNDLE_DIALOG_DELETE_URI, uri)
        args.putBoolean(BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL, false)
        val deleteDialog = DeletePhotoDialog()
        deleteDialog.arguments = args
        return deleteDialog
    }

    fun runDetailActivity(uri: String, date: Long) {
        router.startPhotoDetailActivity(uri, date)
    }

    fun turnOffQrDetector() {
        prefs.turnOnQRDetector(false)
    }

}