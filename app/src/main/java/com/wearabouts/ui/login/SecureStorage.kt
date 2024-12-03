package com.wearabouts.ui.login

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object SecureStorage {
    private const val FILE_NAME = "secure_prefs"
    private const val KEY_USERNAME = "username"
    private const val KEY_PASSWORD = "password"

    private fun getSharedPreferences(context: Context) =
        EncryptedSharedPreferences.create(
            FILE_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun saveLoginInfo(context: Context, username: String, password: String) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putString(KEY_USERNAME, username).putString(KEY_PASSWORD, password).apply()
    }

    fun getLoginInfo(context: Context): Pair<String, String>? {
        val prefs = getSharedPreferences(context)
        val username = prefs.getString(KEY_USERNAME, null)
        val password = prefs.getString(KEY_PASSWORD, null)
        return if (username != null && password != null) {
            Pair(username, password)
        } else {
            null
        }
    }

    fun clearLoginInfo(context: Context) {
        val prefs = getSharedPreferences(context)
        prefs.edit().clear().apply()
    }
}