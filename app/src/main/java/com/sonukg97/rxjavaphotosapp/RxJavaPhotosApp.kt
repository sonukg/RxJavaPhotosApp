package com.sonukg97.rxjavaphotosapp

import android.app.Application
import com.sonukg97.rxjavaphotosapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RxJavaPhotosApp:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RxJavaPhotosApp)
            modules(appModule)
        }
    }
}