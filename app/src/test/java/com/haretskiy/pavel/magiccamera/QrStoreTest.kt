package com.haretskiy.pavel.magiccamera

import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.storage.BarCodeStoreDao
import com.haretskiy.pavel.magiccamera.storage.BarCodeStoreDao_Impl
import com.haretskiy.pavel.magiccamera.storage.BarCodeStoreImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QrStoreTest {

    private val email = "test@email.com"

    @Mock
    private lateinit var mockedStoreDao: BarCodeStoreDao

    private lateinit var store: BarCodeStore

    @Before
    fun setUp() {
        mockedStoreDao = Mockito.mock(BarCodeStoreDao_Impl::class.java)
        store = BarCodeStoreImpl(mockedStoreDao)
    }

    @Test
    fun insertDeleteDataTest() {
//        store.deleteAll()
//        verify(mockedStoreDao).deleteAll()
    }

    @Test
    fun getDataTest() {
        store.getAllUserCodes(email)
        verify(mockedStoreDao).getAllUserCodes(email)
        store.getAll()
        verify(mockedStoreDao).all
    }

}