package com.sonukg97.rxjavaphotosapp.di

import com.sonukg97.rxjavaphotosapp.data.api.RetroClient
import com.sonukg97.rxjavaphotosapp.data.api.ApiService
import com.sonukg97.rxjavaphotosapp.data.remote.AppDatabase
import com.sonukg97.rxjavaphotosapp.data.repository.PhotoRepository
import com.sonukg97.rxjavaphotosapp.view.PhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetroClient().apiService }
    single { AppDatabase.getDatabase(get()) }
    single { get<AppDatabase>().photoDao() }
    single { PhotoRepository(get(), get()) }
    viewModel { PhotoViewModel(get()) }
}