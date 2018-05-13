package com.haretskiy.pavel.magiccamera.viewModels

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.storage.BarCodeStore
import com.haretskiy.pavel.magiccamera.storage.ShareContainer
import com.haretskiy.pavel.magiccamera.ui.dialogs.ClearDbDialog
import com.haretskiy.pavel.magiccamera.ui.dialogs.DeleteAccountDialog
import com.haretskiy.pavel.magiccamera.utils.ImageSaverImpl
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.ImageSaver
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router

class SettingsViewModel(private val mAuth: FirebaseAuth,
                        private val prefs: Prefs,
                        private val router: Router,
                        private val imageSaver: ImageSaver,
                        private val barCodeStore: BarCodeStore,
                        private val shareContainer: ShareContainer) : ViewModel() {

    val userInfo: MutableLiveData<String> = MutableLiveData()

    private var listener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener { }

    fun logOut() {
        mAuth.removeAuthStateListener(listener)
        listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                userInfo.postValue(SIGN_OUT_CODE)
                mAuth.removeAuthStateListener(listener)
            }
        }
        mAuth.addAuthStateListener(listener)
        mAuth.signOut()
    }

    fun deleteAccount(activity: Activity, fm: FragmentManager) {
        mAuth.removeAuthStateListener(listener)
        listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {

                barCodeStore.deleteAllUserCodes(prefs.getUserEmail())

                imageSaver.deleteAllUserPhotos(prefs.getUserEmail(), object : ImageSaverImpl.DeletingPhotoListener {
                    override fun onSuccess() {
                        activity.runOnUiThread {
                            userInfo.postValue(SIGN_OUT_CODE)
                            mAuth.removeAuthStateListener(listener)
                        }
                    }

                    override fun onError(errorMessage: String) {}
                })
            }
        }
        mAuth.addAuthStateListener(listener)
        DeleteAccountDialog().show(fm, DELETE_ACCOUNT_DIALOG)
    }

    fun signOut() {
        shareContainer.clearContainer()
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

    fun getUser() = "$CURRENT_USER ${prefs.getUserEmail()}"

    fun setCameraCore(coreID: Int) {
        prefs.setCameraCoreId(coreID)
    }

    fun getCameraCoreId() = prefs.getCameraCoreId()

}
