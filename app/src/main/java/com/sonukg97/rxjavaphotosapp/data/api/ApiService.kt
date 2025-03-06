package com.sonukg97.rxjavaphotosapp.data.api

import com.sonukg97.rxjavaphotosapp.data.remote.Photo
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    fun getPhotos(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int
    ): Single<List<Photo>>
}