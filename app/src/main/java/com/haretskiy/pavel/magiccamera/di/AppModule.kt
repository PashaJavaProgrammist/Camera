package com.haretskiy.pavel.magiccamera.di

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_SIGN
import com.haretskiy.pavel.magiccamera.ui.fragments.LoginFragment
import org.koin.dsl.context.ParameterProvider
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val appModule: Module = applicationContext {

    factory { params: ParameterProvider ->
        signFragment(params[BUNDLE_KEY_SIGN])
    }
    bean { FirebaseAuth.getInstance() }

}

val modules = listOf(appModule)

private fun signFragment(isSignIn: String): LoginFragment {
    val args = Bundle()
    args.putString(BUNDLE_KEY_SIGN, isSignIn)
    val frag = LoginFragment()
    frag.arguments = args
    return frag
}