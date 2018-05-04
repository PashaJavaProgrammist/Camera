package com.haretskiy.pavel.magiccamera.storage

import com.haretskiy.pavel.magiccamera.models.BarCode

class BarCodeStoreImpl(private val barCodeDao: BarCodeStoreDao) : BarCodeStore {

    override fun getAll() = barCodeDao.all

    override fun getAllUserCodes(userMail: String) = barCodeDao.getAllUserCodes(userMail)

    override fun insert(barCode: BarCode): Unit = Thread({ barCodeDao.insert(barCode) }).start()

    override fun deleteByCode(code: String): Unit = Thread({ barCodeDao.deleteByCode(code) }).start()

    override fun deleteBarCode(code: BarCode): Unit = Thread({ barCodeDao.deleteBarCode(code) }).start()

    override fun deleteAll(): Unit = Thread({ barCodeDao.deleteAll() }).start()

    override fun deleteAllUserCodes(userMail: String): Unit = Thread({ barCodeDao.deleteAllUserCodes(userMail) }).start()
}