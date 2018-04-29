package com.haretskiy.pavel.magiccamera

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.di_koin.modules
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin


class App : Application() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules)
        Fabric.with(this, Crashlytics())
    }
}
