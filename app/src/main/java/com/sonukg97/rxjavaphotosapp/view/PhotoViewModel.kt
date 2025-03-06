package com.sonukg97.rxjavaphotosapp.view

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sonukg97.rxjavaphotosapp.data.repository.PhotoRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class PhotoViewModel(
    private val repository: PhotoRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val photos = Pager(
        config = PagingConfig(pageSize = 20,
            enablePlaceholders = false),
        pagingSourceFactory = { repository.getPhotosFromDb() }
    ).flow.cachedIn(viewModelScope)

    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val errorMessage: MutableState<String?> = mutableStateOf(null)

    init {
        fetchPhotos(1, 20)
    }

    /*private fun fetchPhotos(page: Int, limit: Int) {
        isLoading.value = true
        repository.getPhotos(page, limit)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { isLoading.value = false },
                { error ->
                    error.printStackTrace()
                    errorMessage.value = error.message
                    isLoading.value = false
                }
            )
            .let { compositeDisposable.add(it) }
    }*/

    /*fun fetchPhotos(page: Int, limit: Int) {
        isLoading.value = true
        repository.clearAndFetchPhotos(page, limit)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { isLoading.value = false },
                { error ->
                    error.printStackTrace()
                    errorMessage.value = error.message
                    isLoading.value = false
                }
            )
            .let { compositeDisposable.add(it) }
    }*/

    private fun fetchPhotos(page: Int, limit: Int) {
        repository.getPhotos(page, limit)
            .subscribeOn(Schedulers.io())
            .subscribe({}, { it.printStackTrace() })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}