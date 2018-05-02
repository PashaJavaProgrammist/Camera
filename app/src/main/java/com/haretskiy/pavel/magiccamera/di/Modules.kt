package com.haretskiy.pavel.magiccamera.di

import android.arch.persistence.room.Room
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.WindowManager
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_SIGN
import com.haretskiy.pavel.magiccamera.DB_NAME
import com.haretskiy.pavel.magiccamera.camera2Api.Camera2Helper
import com.haretskiy.pavel.magiccamera.cameraApi.CameraHolderCallback
import com.haretskiy.pavel.magiccamera.googleVisionApi.googleVisionUtlis.CameraSourceManager
import com.haretskiy.pavel.magiccamera.storage.*
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionDialog
import com.haretskiy.pavel.magiccamera.ui.fragments.*
import com.haretskiy.pavel.magiccamera.utils.*
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageLoader
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import com.haretskiy.pavel.magiccamera.viewModels.*
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.context.ParameterProvider
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val appModule: Module = applicationContext {

    factory { params: ParameterProvider ->
        signFragment(params[BUNDLE_KEY_SIGN])
    }
    factory { GoogleVisionFragment() }
    factory { GalleryFragment() }
    factory { SettingsFragment() }
    bean { FirebaseAuth.getInstance() }
    factory { PermissionDialog() }

    bean { Room.databaseBuilder(androidApplication(), Database::class.java, DB_NAME).build() }

    bean { PhotoStoreImpl((get() as Database).photoStoreDao()) as PhotoStore }
    bean { BarCodeStoreImpl((get() as Database).barCodeDao()) as BarCodeStore }

    factory { DiffCallBack() }

    viewModel { LoginViewModel(get()) }
    viewModel { GalleryViewModel(androidApplication(), get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { PhotoDetailViewModel(get(), get()) }
    viewModel { QrHistoryVewModel(get(), get()) }
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val camera2Module: Module = applicationContext {
    factory { Camera2Fragment() }
    factory {
        androidApplication().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }
    factory {
        androidApplication().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    factory { Camera2Helper(androidApplication(), get(), get(), get(), get(), get()) }

}

val cameraModule: Module = applicationContext {
    factory { CameraFragment() }
    factory { CameraHolderCallback(get()) }
}

val googleVisionModule: Module = applicationContext {
    factory { GoogleApiAvailability.getInstance() }
    factory { CameraSourceManager(androidApplication(), get(), get(), get(), get()) }
}

val utilsModule: Module = applicationContext {
    factory { ComparatorAreas() }
    bean { RouterImpl(androidApplication()) as Router }
    bean { Prefs(androidApplication()) }
    factory { Toaster(androidApplication()) }
    factory { ImageSaverImpl(androidApplication(), get(), get(), get()) as ImageSaver }
    factory { GlideImageLoaderImpl(androidApplication()) as ImageLoader }
}

val modules = listOf(appModule, camera2Module, cameraModule, googleVisionModule, utilsModule)

private fun signFragment(isSignIn: String): LoginFragment {
    val args = Bundle()
    args.putString(BUNDLE_KEY_SIGN, isSignIn)
    val frag = LoginFragment()
    frag.arguments = args
    return frag
}