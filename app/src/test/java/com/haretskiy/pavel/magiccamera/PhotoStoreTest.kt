package com.haretskiy.pavel.magiccamera

import com.haretskiy.pavel.magiccamera.storage.PhotoStore
import com.haretskiy.pavel.magiccamera.storage.PhotoStoreDao
import com.haretskiy.pavel.magiccamera.storage.PhotoStoreDao_Impl
import com.haretskiy.pavel.magiccamera.storage.PhotoStoreImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotoStoreTest {

    private val email = "test@email.com"
    private val uri = "0/storage/file.jpg"

    @Mock
    private lateinit var mockedStoreDao: PhotoStoreDao

    private lateinit var store: PhotoStore

    @Before
    fun setUp() {
        mockedStoreDao = Mockito.mock(PhotoStoreDao_Impl::class.java)
        store = PhotoStoreImpl(mockedStoreDao)
    }

    @Test
    fun insertDeleteDataTest() {
//        store.deleteAll()
//        verify(mockedStoreDao).deleteAll()
//        store.savePhoto(uri, 10, email)
//        val photo = Photo(10, uri, email)
//        verify(mockedStoreDao).insert(photo)
    }

    @Test
    fun getDataTest() {
        store.getAllUserPhotosList(email)
        verify(mockedStoreDao).getUserPhotosList(email)
        store.getAllUserPhotosDataSourceFactory(email)
        verify(mockedStoreDao).getUserPhotosDataSourceFactory(email)
        store.getAllUserPhotosLiveData(email)
        verify(mockedStoreDao).getUserPhotosLiveDataList(email)
    }

}