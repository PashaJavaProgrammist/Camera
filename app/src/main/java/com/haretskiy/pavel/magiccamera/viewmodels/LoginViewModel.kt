package com.haretskiy.pavel.magiccamera.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crash.FirebaseCrash
import com.haretskiy.pavel.magiccamera.models.FirebaseLoginResponse

class LoginViewModel(private val mAuth: FirebaseAuth) : ViewModel() {

    val userInfo: MutableLiveData<FirebaseLoginResponse> = MutableLiveData()

    fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
    }

    fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
    }

    private val onCompleteListener = OnCompleteListener<AuthResult> { task ->
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            userInfo.postValue(FirebaseLoginResponse(mAuth.currentUser, ""))
        } else {
            // If sign in fails, display a message to the user.
            userInfo.postValue(FirebaseLoginResponse(null, "${task.exception}"))
            FirebaseCrash.report(task.exception)
        }
    }

}

