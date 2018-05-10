package com.haretskiy.pavel.magiccamera

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.core.CrashlyticsCore
import com.haretskiy.pavel.magiccamera.di.modules
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules)

        Fabric.with(this, Answers())
        val crashlyticsKit = Crashlytics.Builder()
                .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build()

        Fabric.with(this, crashlyticsKit)
    }
}
