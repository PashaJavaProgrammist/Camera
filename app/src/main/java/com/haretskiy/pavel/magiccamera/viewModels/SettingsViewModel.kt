package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.SIGN_OUT_CODE
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class SettingsViewModel(private val mAuth: FirebaseAuth,
                        private val prefs: Prefs,
                        private val router: Router) : ViewModel() {

    val userInfo: MutableLiveData<String> = MutableLiveData()

    fun logOut() {
        mAuth.currentUser
        mAuth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                userInfo.postValue(SIGN_OUT_CODE)
            }
        }
        mAuth.signOut()
    }

    fun signOut() {
        prefs.setUserStateLogOut()
        router.startLoginActivity()
    }

    fun startGoToSettings() {
        router.startSettingsActivity()
    }

}
