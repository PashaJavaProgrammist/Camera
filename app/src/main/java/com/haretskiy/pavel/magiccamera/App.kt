package com.haretskiy.pavel.magiccamera

import android.app.Application
import com.haretskiy.pavel.magiccamera.di.modules
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules)
    }
}