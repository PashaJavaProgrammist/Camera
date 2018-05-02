package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.haretskiy.pavel.magiccamera.models.BarCode
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.utils.Prefs

class QrHistoryVewModel(private val barCodeStore: BarCodeStore,
                        private val prefs: Prefs) : ViewModel() {

    val storageBarCodesLiveData: LiveData<List<BarCode>> by lazy {
        barCodeStore.getAllUserCodes(prefs.getUserEmail())
    }

}