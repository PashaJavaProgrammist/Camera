package com.haretskiy.pavel.magiccamera.storage

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.haretskiy.pavel.magiccamera.models.BarCode

@Dao
interface BarCodeStoreDao {

    @get:Query("SELECT * FROM barcodes ORDER BY date DESC")
    val all: LiveData<List<BarCode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(barCode: BarCode)

    @Query("SELECT * FROM barcodes WHERE userEmail = :userMail ORDER BY date DESC")
    fun getAllUserCodes(userMail: String): LiveData<List<BarCode>>

    @Query("SELECT * FROM barcodes WHERE userEmail = :userMail ORDER BY date DESC")
    fun getAllUserCodesList(userMail: String): List<BarCode>

    @Query("DELETE FROM barcodes WHERE code = :code")
    fun deleteByCode(code: String)

    @Query("DELETE FROM barcodes WHERE userEmail = :email")
    fun deleteAllUserCodes(email: String)

    @Delete
    fun deleteBarCode(code: BarCode)

    @Query("DELETE FROM barcodes")
    fun deleteAll()
}