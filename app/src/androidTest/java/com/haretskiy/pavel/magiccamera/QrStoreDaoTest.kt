package com.haretskiy.pavel.magiccamera

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.haretskiy.pavel.magiccamera.models.BarCode
import com.haretskiy.pavel.magiccamera.storage.BarCodeStoreDao
import com.haretskiy.pavel.magiccamera.storage.Database
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QrStoreDaoTest {

    private lateinit var db: Database
    private lateinit var storeDao: BarCodeStoreDao

    @Before
    @Throws(Exception::class)
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                Database::class.java)
                .build()
        storeDao = db.barCodeDao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertCodeThenReadTheSameOneTest() {
        val code = "12345678"
        val email = "test@mail.com"
        val qr = BarCode(code, email, 23)

        storeDao.insert(qr)
        val list = storeDao.getAllUserCodesList(email)

        assertEquals(1, list.size)
        assertTrue(list[0].code == code)
        assertTrue(list[0].userEmail == email)

        storeDao.deleteAllUserCodes(email)
        val listD = storeDao.getAllUserCodesList(email)
        assertEquals(0, listD.size)
    }

}