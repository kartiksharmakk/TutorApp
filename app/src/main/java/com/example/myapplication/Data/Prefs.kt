package com.example.myapplication.Data

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private const val PREF_NAME = "MY_SHARED_PREFERENCES"
    private lateinit var sharedPreferences: SharedPreferences

    private const val KEY_USERNAME = "username"
    private const val DEFAULT_USERNAME = ""
    private const val KEY_LOGGED_IN = "loggedIn"
    private const val IS_LOGGED_IN = false
    private const val VERIFICATION_ID_KEY = "verificationId"


    var username: String
        get() = sharedPreferences.getString(KEY_USERNAME, DEFAULT_USERNAME) ?: DEFAULT_USERNAME
        set(value) = sharedPreferences.edit().putString(KEY_USERNAME, value).apply()

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveVerificationId(context: Context, verificationId: String) {
        getPrefs(context).edit().putString(VERIFICATION_ID_KEY, verificationId).apply()
    }

    fun getVerificationId(context: Context): String? {
        return getPrefs(context).getString(VERIFICATION_ID_KEY, null)
    }
    fun clearPrefs() {
        sharedPreferences.edit().clear().apply()
    }
}