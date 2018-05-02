package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.utils.Prefs

class PhotoDetailViewModel(private val photoStore: PhotoStore,
                           private val prefs: Prefs) : ViewModel() {

    val storagePhotosLiveData: LiveData<List<Photo>> by lazy {
        photoStore.getAllUserPhotosLiveData(prefs.getUserEmail())
    }

}