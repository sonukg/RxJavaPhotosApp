package com.sonukg97.rxjavaphotosapp.data.remote

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<Photo>)

    @Query("SELECT * FROM photos ORDER BY id ASC")
    fun getPhotos(): PagingSource<Int, Photo>

    /*@Query("SELECT * FROM photos")
    fun getPhotos(): PagingSource<Int, Photo>*/

    @Query("DELETE FROM photos")
    fun clearPhotos()
}