package com.haretskiy.pavel.magiccamera.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.storage.ShareContainer
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeletePhotoDialog
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeletePhotosDialog
import com.haretskiy.pavel.magiccamera.utils.ImageSaverImpl
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.DeleteListener
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class GalleryViewModel(app: Application,
                       private val photoStore: PhotoStore,
                       private val prefs: Prefs,
                       private val router: Router,
                       private val shareContainer: ShareContainer) : AndroidViewModel(app) {

    val checkedPhotosData: MutableLiveData<Int> = MutableLiveData()

    var listOfPhotos = emptyList<Photo>()

    override fun onCleared() {
        super.onCleared()
        shareContainer.clearContainer()
    }

    fun getAllUserPhotosLiveData() = LivePagedListBuilder(
            photoStore.getAllUserPhotosDataSourceFactory(prefs.getUserEmail()),
            PagedList.Config.Builder()
                    .setPageSize(PAGE_SIZE)
                    .setInitialLoadSizeHint(PAGE_SIZE_HINT)
                    .setPrefetchDistance(PREFETCH_DISTANCE)
                    .setEnablePlaceholders(false)
                    .build())
            .build()

    fun deletePhoto(fm: FragmentManager, uri: String, listener: DeleteListener) {
        newDeleteDialogInstance(uri).show(fm, FRAGMENT_DIALOG_DELETE, listener)
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

    fun fillShareContainer(uri: String, listener: OnSelectedPhotoListener) {
        if (shareContainer.isContains(uri)) {
            shareContainer.removeItem(uri)
            listener.onUnchecked()
        } else {
            shareContainer.addItem(uri)
            listener.onChecked()
        }
        checkedPhotosData.postValue(shareContainer.getCountOfItems())
    }

    fun isPhotoAlreadySelected(uri: String) = shareContainer.isContains(uri)

    fun isAtLeastOnePhotoSelected() = shareContainer.isAtLeastOnePhotoSelected()

    fun deleteSelectedPhotos(fm: FragmentManager, listener: ImageSaverImpl.DeletingPhotoListener) {
        DeletePhotosDialog().show(fm, DELETE_PHOTOS_DIALOG, listener)
    }

    fun shareImages() {
        router.shareImages(shareContainer.getAllUris())
    }

    fun clearSelectedItems() {
        shareContainer.clearContainer()
    }

    fun selectAllItems() {
        shareContainer.selectAll(listOfPhotos)
    }

    fun allItemsSelected() = listOfPhotos.size == shareContainer.getCountOfItems()

    interface OnSelectedPhotoListener {
        fun onChecked()
        fun onUnchecked()
    }

}