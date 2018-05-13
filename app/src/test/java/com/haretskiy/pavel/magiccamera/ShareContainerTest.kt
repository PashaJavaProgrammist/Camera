package com.haretskiy.pavel.magiccamera

import com.haretskiy.pavel.magiccamera.storage.ShareContainer
import com.haretskiy.pavel.magiccamera.storage.ShareContainerImpl
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShareContainerTest {

    val uri1 = "uri1"
    val uri2 = "uri2"
    val uri3 = "uri3"

    private lateinit var shareContainer: ShareContainer

    @Before
    fun setUp() {
        shareContainer = ShareContainerImpl()
    }

    @Test
    fun totalTest() {
        shareContainer.addItem(uri1)
        shareContainer.addItem(uri2)
        shareContainer.addItem(uri3)
        Assert.assertEquals(3, shareContainer.getCountOfItems())

        shareContainer.clearContainer()
        Assert.assertEquals(0, shareContainer.getCountOfItems())

        shareContainer.addItem(uri1)
        shareContainer.addItem(uri2)
        shareContainer.removeItem(uri2)
        val uriFromContainer = shareContainer.getAllUris()[0]
        Assert.assertEquals(uri1, uriFromContainer)
    }

}