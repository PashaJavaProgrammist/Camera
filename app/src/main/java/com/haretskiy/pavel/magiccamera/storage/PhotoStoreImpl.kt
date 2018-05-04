package com.haretskiy.pavel.magiccamera.storage

import com.haretskiy.pavel.magiccamera.models.Photo

class PhotoStoreImpl(private val photoDao: PhotoStoreDao) : PhotoStore {

    override fun savePhoto(uri: String, date: Long, email: String): Unit = Thread({ photoDao.insert(Photo(date, uri, email)) }).start()

    override fun getAllUserPhotosDataSourceFactory(userEmail: String) = photoDao.getUserPhotosDataSourceFactory(userEmail)

    override fun getAllUserPhotosLiveData(userEmail: String) = photoDao.getUserPhotosLiveDataList(userEmail)

    override fun getAllPhotosDataSourceFactory() = photoDao.all

    override fun getPhotoById(id: Long) = photoDao.getPhotoById(id)

    override fun deletePhoto(uri: String): Unit = Thread({ photoDao.deleteByUri(uri) }).start()

    override fun deleteAll(): Unit = Thread({ photoDao.deleteAll() }).start()

    override fun deleteAllUserPhoto(userEmail: String): Unit = Thread({ photoDao.deleteAllUserPhotos(userEmail) }).start()

}