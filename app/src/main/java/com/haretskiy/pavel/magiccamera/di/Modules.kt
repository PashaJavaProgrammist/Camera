package com.haretskiy.pavel.magiccamera.di

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.WindowManager
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.gms.vision.face.FaceDetector
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_SIGN
import com.haretskiy.pavel.magiccamera.camera2Api.Camera2Helper
import com.haretskiy.pavel.magiccamera.cameraApi.CameraHolderCallback
import com.haretskiy.pavel.magiccamera.googleVisioApi.barcodeDerector.BarcodeTrackerFactory
import com.haretskiy.pavel.magiccamera.googleVisioApi.faceDetector.FaceTrackerFactory
import com.haretskiy.pavel.magiccamera.navigation.Router
import com.haretskiy.pavel.magiccamera.navigation.RouterImpl
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionDialog
import com.haretskiy.pavel.magiccamera.ui.fragments.*
import com.haretskiy.pavel.magiccamera.utils.ComparatorSizesByArea
import com.haretskiy.pavel.magiccamera.utils.ImageSaver
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.viewmodels.LoginViewModel
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
    viewModel { LoginViewModel(get()) }
    bean { RouterImpl(androidApplication()) as Router }
    bean { Prefs(androidApplication()) }
    factory { Toaster(androidApplication()) }
    factory { PermissionDialog() }
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
    factory { ImageSaver(androidApplication(), get()) }
    factory { ComparatorSizesByArea() }
    factory { Camera2Helper(androidApplication(), get(), get(), get(), get(), get()) }
}

val cameraModule: Module = applicationContext {
    factory { CameraFragment() }
    factory { CameraHolderCallback(get()) }
}

val googleVisioModule: Module = applicationContext {
    factory { BarcodeTrackerFactory() }
    factory { BarcodeDetector.Builder(androidApplication()).build() }

    factory { FaceTrackerFactory() }
    factory {
        FaceDetector.Builder(androidApplication())
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setTrackingEnabled(true)
                .build()
    }

    factory { GoogleApiAvailability.getInstance() }
}

val modules = listOf(appModule, camera2Module, cameraModule, googleVisioModule)

private fun signFragment(isSignIn: String): LoginFragment {
    val args = Bundle()
    args.putString(BUNDLE_KEY_SIGN, isSignIn)
    val frag = LoginFragment()
    frag.arguments = args
    return frag
}