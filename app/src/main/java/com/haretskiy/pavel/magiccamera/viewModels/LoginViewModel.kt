package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crash.FirebaseCrash
import com.haretskiy.pavel.magiccamera.EMPTY_STRING
import com.haretskiy.pavel.magiccamera.FIELDS_ARE_EMPTY
import com.haretskiy.pavel.magiccamera.PASSWORDS_DO_NOT_MATCH
import com.haretskiy.pavel.magiccamera.models.FirebaseLoginResponse
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.Toaster
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class LoginViewModel(private val mAuth: FirebaseAuth,
                     private val router: Router,
                     private val prefs: Prefs,
                     private val toaster: Toaster) : ViewModel() {

    val userInfo: MutableLiveData<FirebaseLoginResponse> = MutableLiveData()

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
    }

    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener)
    }

    private val onCompleteListener = OnCompleteListener<AuthResult> { task ->
        if (task.isSuccessful) {
            // Sign in success, update UI with the signed-in user's information
            userInfo.postValue(FirebaseLoginResponse(mAuth.currentUser, EMPTY_STRING))
        } else {
            // If sign in fails, display a message to the user.
            userInfo.postValue(FirebaseLoginResponse(null, "${task.exception}"))
            FirebaseCrash.report(task.exception)
        }
    }

    fun sign(emailStr: String, passwordStr: String, repeatPasswordStr: String) {
        if (!emailStr.isEmpty() && !passwordStr.isEmpty()) {
            when (repeatPasswordStr == EMPTY_STRING) {
                true -> signIn(emailStr, passwordStr)
                false -> {
                    if (passwordStr == repeatPasswordStr) {
                        signUp(emailStr, passwordStr)
                    } else {
                        userInfo.postValue(FirebaseLoginResponse(null, PASSWORDS_DO_NOT_MATCH))
                    }
                }
            }
        } else {
            userInfo.postValue(FirebaseLoginResponse(null, FIELDS_ARE_EMPTY))
        }
    }

    fun onSuccessAuth(email: String) {
        toaster.showToast(email, false)
        prefs.setUserStateLogIn()
        prefs.saveEmail(email)
        router.startHostActivity()
    }

    fun onErrorAuth(errorMessages: String) {
        toaster.showToast(errorMessages, true)
    }

}

