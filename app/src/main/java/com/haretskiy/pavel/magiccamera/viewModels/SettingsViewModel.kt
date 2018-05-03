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
            override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                if (firebaseAuth.currentUser == null) {
                    userInfo.postValue(SIGN_OUT_CODE)
                }
            }
        })
        mAuth.signOut()

    }

}
