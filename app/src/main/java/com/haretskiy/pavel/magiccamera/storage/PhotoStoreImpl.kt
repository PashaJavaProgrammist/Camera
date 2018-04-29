package com.haretskiy.pavel.magiccamera.storage

import com.haretskiy.pavel.magiccamera.models.ImageModel

class PhotoStoreImpl(private val dao: PhotoStoreDao) : Store {

    override fun savePhoto(uri: String, date: Long) {
        Thread({ dao.insert(ImageModel(0, date, uri)) }).start()
    }

    override fun getAllPhotosList() = dao.all

    override fun getPhotoById(id: Long) = dao.getPhotoById(id)
}