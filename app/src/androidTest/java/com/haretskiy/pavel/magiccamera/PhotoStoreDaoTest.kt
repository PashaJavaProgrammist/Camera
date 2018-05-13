package com.haretskiy.pavel.magiccamera

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.haretskiy.pavel.magiccamera.models.Photo
import com.haretskiy.pavel.magiccamera.storage.Database
import com.haretskiy.pavel.magiccamera.storage.PhotoStoreDao
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoStoreDaoTest {

    private lateinit var db: Database
    private lateinit var storeDao: PhotoStoreDao

    @Before
    @Throws(Exception::class)
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                Database::class.java)
                .build()
        storeDao = db.photoStoreDao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertPhotoThenReadTheSameOneTest() {

        val email = "testEmail@mail.com"
        val uri = "/0/storage/pictures/file.jpg"

        val photoToInsert = Photo(10, uri, email)

        storeDao.insert(photoToInsert)
        val listPostInsert = storeDao.getUserPhotosList(email)

        assertEquals(1, listPostInsert.size)
        assertTrue(listPostInsert[0].uri == uri)
        assertTrue(listPostInsert[0].userEmail == email)
    }

    @Test
    @Throws(Exception::class)
    fun insertThenDeleteTest() {

        val email = "testEmail@mail.com"
        val uri = "/0/storage/pictures/file.jpg"

        val photoToInsert1 = Photo(10, uri + "1", email)
        val photoToInsert2 = Photo(1230, uri + "2", email)
        val photoToInsert3 = Photo(1320, uri + "3", email)
        storeDao.deleteAll()
        storeDao.insert(photoToInsert1)
        storeDao.insert(photoToInsert2)
        storeDao.insert(photoToInsert3)
        val listPostInsert = storeDao.getUserPhotosList(email)
        assertEquals(3, listPostInsert.size)
        storeDao.deleteAll()
        val listPostDelete = storeDao.getUserPhotosList(email)
        assertEquals(0, listPostDelete.size)
    }

    @Test
    @Throws(Exception::class)
    fun insertByEmailThenGetTest() {

        val email1 = "testEmail1@mail.com"
        val email2 = "testEmail2@mail.com"
        val uri = "/0/storage/pictures/file.jpg"

        val photoToInsert1 = Photo(10, uri + "1", email1)
        val photoToInsert2 = Photo(1230, uri + "2", email1)
        val photoToInsert3 = Photo(1320, uri + "3", email1)
        val photoToInsert4 = Photo(10, uri + "4", email1)
        val photoToInsert5 = Photo(12303, uri + "5", email2)
        val photoToInsert6 = Photo(133420, uri + "6", email2)
        storeDao.deleteAll()
        storeDao.insert(photoToInsert1)
        storeDao.insert(photoToInsert2)
        storeDao.insert(photoToInsert3)
        storeDao.insert(photoToInsert4)
        storeDao.insert(photoToInsert5)
        storeDao.insert(photoToInsert6)
        val list1 = storeDao.getUserPhotosList(email1)
        assertEquals(4, list1.size)
        val list2 = storeDao.getUserPhotosList(email2)
        assertEquals(2, list2.size)
    }

}