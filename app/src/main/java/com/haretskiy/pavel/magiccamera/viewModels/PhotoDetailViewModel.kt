package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.storage.Store
import com.haretskiy.pavel.magiccamera.utils.Prefs

class PhotoDetailViewModel(private val store: Store,
                           private val prefs: Prefs) : ViewModel() {

    val storagePhotosLiveData: LiveData<List<Photo>> by lazy {
        store.getAllUserPhotosLiveData(prefs.getUserEmail())
    }

}