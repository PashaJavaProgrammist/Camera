package com.haretskiy.pavel.magiccamera.viewModels

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL
import com.haretskiy.pavel.magiccamera.BUNDLE_DIALOG_DELETE_URI
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeletePhotoDialog
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.Printer
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router


class PhotoDetailViewModel(private val photoStore: PhotoStore,
                           private val prefs: Prefs,
                           private val router: Router,
                           private val printer: Printer) : ViewModel() {

    val storagePhotosLiveData: LiveData<List<Photo>> by lazy {
        photoStore.getAllUserPhotosLiveData(prefs.getUserEmail())
    }


    fun newDeleteDialogInstance(uri: String): DeletePhotoDialog {
        val args = Bundle()
        args.putString(BUNDLE_DIALOG_DELETE_URI, uri)
        args.putBoolean(BUNDLE_DIALOG_DELETE_IS_PHOTO_DETAIL, true)
        val deleteDialog = DeletePhotoDialog()
        deleteDialog.arguments = args
        return deleteDialog
    }

    fun shareImage(uri: String) {
        router.shareImage(uri)
    }

    fun doPhotoPrint(activity: Activity, uri: String) {
        printer.printPhoto(activity, uri)
    }
}