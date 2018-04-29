package com.haretskiy.pavel.magiccamera.storage

import com.haretskiy.pavel.magiccamera.models.Photo

class PhotoStoreImpl(private val dao: PhotoStoreDao) : Store {

    override fun savePhoto(uri: String, date: Long, email: String): Unit = Thread({ dao.insert(Photo(date, uri, email)) }).start()

    override fun getAllUserPhotos(userEmail: String) = dao.getUsersPhotos(userEmail)

    override fun getAllPhotosList() = dao.all

    override fun getPhotoById(id: Long) = dao.getPhotoById(id)

    override fun deletePhoto(uri: String) {
        dao.deleteByUri(uri)
    }

    override fun deleteAll() {
        dao.deleteAll()
    }
}