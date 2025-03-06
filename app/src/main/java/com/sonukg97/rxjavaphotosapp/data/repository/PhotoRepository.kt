package com.sonukg97.rxjavaphotosapp.data.repository

import io.reactivex.rxjava3.core.Single
import androidx.paging.PagingSource
import com.sonukg97.rxjavaphotosapp.data.api.ApiService
import com.sonukg97.rxjavaphotosapp.data.remote.Photo
import com.sonukg97.rxjavaphotosapp.data.remote.PhotoDao

class PhotoRepository(
    private val apiService: ApiService,
    private val photoDao: PhotoDao
) {
    fun getPhotos(page: Int, limit: Int): Single<List<Photo>> {
        return apiService.getPhotos(page, limit)
            .doOnSuccess { photos ->
                photoDao.insertPhotos(photos)
            }
    }

    fun getPhotosFromDb(): PagingSource<Int, Photo> {
        return photoDao.getPhotos()
    }

    fun clearPhotos() {
        photoDao.clearPhotos()
    }

    fun clearAndFetchPhotos(page: Int, limit: Int): Single<List<Photo>> {
        return Single.fromCallable {
            photoDao.clearPhotos()
        }.flatMap {
            getPhotos(page, limit)
        }
    }

}