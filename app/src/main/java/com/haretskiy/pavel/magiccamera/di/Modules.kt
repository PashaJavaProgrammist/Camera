package com.haretskiy.pavel.magiccamera.di

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_SIGN
import com.haretskiy.pavel.magiccamera.navigation.Router
import com.haretskiy.pavel.magiccamera.navigation.RouterImpl
import com.haretskiy.pavel.magiccamera.ui.dialogs.PermissionDialog
import com.haretskiy.pavel.magiccamera.ui.fragments.GalleryFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.LoginFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.QRFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.SettingsFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.camera2Api.Camera2Fragment
import com.haretskiy.pavel.magiccamera.ui.fragments.camera2Api.Camera2Helper
import com.haretskiy.pavel.magiccamera.ui.fragments.cameraApi.CameraFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.cameraApi.HolderCallback
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
    factory { QRFragment() }
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
    factory { ImageSaver(androidApplication()) }
    factory { ComparatorSizesByArea() }
    factory { Camera2Helper(androidApplication(), get(), get(), get(), get(), get()) }
}

val cameraModule: Module = applicationContext {
    factory { CameraFragment() }
    factory { HolderCallback(get()) }
}

val modules = listOf(appModule, camera2Module, cameraModule)

private fun signFragment(isSignIn: String): LoginFragment {
    val args = Bundle()
    args.putString(BUNDLE_KEY_SIGN, isSignIn)
    val frag = LoginFragment()
    frag.arguments = args
    return frag
}