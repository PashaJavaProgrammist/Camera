package com.haretskiy.pavel.magiccamera.storage

import com.haretskiy.pavel.magiccamera.models.Photo

class PhotoStoreImpl(private val dao: PhotoStoreDao) : Store {

    override fun savePhoto(uri: String, date: Long, email: String): Unit = Thread({ dao.insert(Photo(date, uri, email)) }).start()

    override fun getAllUserPhotosDataSourceFactory(userEmail: String) = dao.getUserPhotosDataSourceFactory(userEmail)

    override fun getAllUserPhotosLiveData(userEmail: String) = dao.getUserPhotosLiveDataList(userEmail)

    override fun getAllPhotosDataSourceFactory() = dao.all

    override fun getPhotoById(id: Long) = dao.getPhotoById(id)

    override fun deletePhoto(uri: String): Unit = Thread({ dao.deleteByUri(uri) }).start()

    override fun deleteAll(): Unit = Thread({ dao.deleteAll() }).start()

    override fun deleteAllUserPhoto(userEmail: String): Unit = Thread({ dao.deleteAllUserPhotos(userEmail) }).start()
}