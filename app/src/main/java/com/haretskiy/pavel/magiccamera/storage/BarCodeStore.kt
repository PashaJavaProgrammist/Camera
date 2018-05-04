package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import com.haretskiy.pavel.magiccamera.models.BarCode

interface BarCodeStore {

    fun getAll(): LiveData<List<BarCode>>

    fun insert(barCode: BarCode)

    fun getAllUserCodes(userMail: String): LiveData<List<BarCode>>

    fun deleteByCode(code: String)

    fun deleteBarCode(code: BarCode)

    fun deleteAll()

    fun deleteAllUserCodes(userMail: String)
}