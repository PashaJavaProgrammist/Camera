package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.SIGN_OUT_CODE

class SettingsViewModel(private val mAuth: FirebaseAuth) : ViewModel() {

    val userInfo: MutableLiveData<String> = MutableLiveData()

    fun logOut() {
        mAuth.currentUser
        mAuth.addAuthStateListener(object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                if (p0.currentUser == null) {
                    userInfo.postValue(SIGN_OUT_CODE)
                }
            }
        })
        mAuth.signOut()

    }

}
