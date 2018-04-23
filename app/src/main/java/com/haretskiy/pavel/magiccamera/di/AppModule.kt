package com.haretskiy.pavel.magiccamera.di

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_SIGN
import com.haretskiy.pavel.magiccamera.navigation.Router
import com.haretskiy.pavel.magiccamera.navigation.RouterImpl
import com.haretskiy.pavel.magiccamera.ui.fragments.CameraFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.GalleryFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.LoginFragment
import com.haretskiy.pavel.magiccamera.ui.fragments.QRFragment
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
    factory { CameraFragment() }
    factory { GalleryFragment() }
    bean { FirebaseAuth.getInstance() }
    viewModel { LoginViewModel(get()) }
    bean { RouterImpl(androidApplication()) as Router }
}

val modules = listOf(appModule)

private fun signFragment(isSignIn: String): LoginFragment {
    val args = Bundle()
    args.putString(BUNDLE_KEY_SIGN, isSignIn)
    val frag = LoginFragment()
    frag.arguments = args
    return frag
}