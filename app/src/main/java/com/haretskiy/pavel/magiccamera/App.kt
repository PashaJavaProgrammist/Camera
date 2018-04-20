package com.haretskiy.pavel.magiccamera

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.di.modules
import org.koin.android.ext.android.startKoin

class App : Application() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules)
    }
}
