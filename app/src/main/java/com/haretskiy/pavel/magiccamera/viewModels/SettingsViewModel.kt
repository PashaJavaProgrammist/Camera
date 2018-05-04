package com.haretskiy.pavel.magiccamera.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.ui.dialogs.ClearDbDialog
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

    fun clearPhotosDB(fm: FragmentManager) {
        newClearDbDialog(TYPE_PHOTO).show(fm, CLEAR_DB_DIALOG)
    }

    fun clearQrDB(fm: FragmentManager) {
        newClearDbDialog(TYPE_QR).show(fm, CLEAR_DB_DIALOG)
    }

    private fun newClearDbDialog(type: String): ClearDbDialog {
        val args = Bundle()
        args.putString(BUNDLE_KEY_TYPE_CLEAR_DB, type)
        val clearDbDialog = ClearDbDialog()
        clearDbDialog.arguments = args
        return clearDbDialog
    }

}
