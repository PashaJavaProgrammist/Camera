package com.haretskiy.pavel.magiccamera.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_EMAIL
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_IS_LOG_IN
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_PREFS_CAMERA_SIZE
import com.haretskiy.pavel.magiccamera.EMPTY_STRING


class Prefs(context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor: SharedPreferences.Editor = preferences.edit()


    fun saveUserCredentials() {}

    fun setUserStateLogIn() {
        save(BUNDLE_KEY_IS_LOG_IN, true)
    }

    fun setUserStateLogOut() {
        save(BUNDLE_KEY_IS_LOG_IN, false)
    }

    fun saveEmail(email: String?) {
        if (email != null) save(BUNDLE_KEY_EMAIL, email)
    }

    fun getUserEmail() = getString(BUNDLE_KEY_EMAIL, EMPTY_STRING)

    fun isUserLogIn() = getBoolean(BUNDLE_KEY_IS_LOG_IN, false)

    fun saveCameraScreenSize(cameraID: String, sizePosition: Int) {
        save("${BUNDLE_KEY_PREFS_CAMERA_SIZE}_$cameraID", sizePosition)
    }

    fun getCameraScreenSizePosition(cameraID: String) = getInt("${BUNDLE_KEY_PREFS_CAMERA_SIZE}_$cameraID", -1)


    //Prefs methods
    private fun save(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    private fun save(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    private fun save(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    private fun save(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    private fun save(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    private fun save(key: String, value: Set<String>) {
        editor.putStringSet(key, value).apply()
    }

    private fun getBoolean(key: String, defValue: Boolean) = preferences.getBoolean(key, defValue)


    private fun getString(key: String, defValue: String) = preferences.getString(key, defValue)


    private fun getInt(key: String, defValue: Int) = preferences.getInt(key, defValue)


    private fun getFloat(key: String, defValue: Float) = preferences.getFloat(key, defValue)


    private fun getLong(key: String, defValue: Long) = preferences.getLong(key, defValue)


    private fun getStringSet(key: String, defValue: Set<String>) = preferences.getStringSet(key, defValue)


    private fun getAll(): Map<String, *> = preferences.all

    private fun remove(key: String) {
        editor.remove(key).apply()
    }
}
