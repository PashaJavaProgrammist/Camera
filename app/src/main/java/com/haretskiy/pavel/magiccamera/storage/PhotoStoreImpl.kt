package com.haretskiy.pavel.magiccamera.storage

import com.haretskiy.pavel.magiccamera.models.ImageModel

class PhotoStoreImpl(private val dao: PhotoStoreDao) : Store {

    override fun savePhoto(uri: String, date: Long, email: String): Unit = Thread({ dao.insert(ImageModel(date, uri, email)) }).start()

    override fun getAllUserPhotos(userEmail: String) = dao.getUsersPhotos(userEmail)

    override fun getAllPhotosList() = dao.all

    override fun getPhotoById(id: Long) = dao.getPhotoById(id)
}