package com.example.myapplication.Data

import android.content.Context
import android.content.SharedPreferences

object Prefs {
    private const val PREF_NAME = "MY_SHARED_PREFERENCES"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private const val KEY_USERNAME = "username"
    private const val DEFAULT_USERNAME = ""
    private const val KEY_LOGGED_IN = "loggedIn"
    private const val IS_LOGGED_IN = false

    var username: String
        get() = sharedPreferences.getString(KEY_USERNAME, DEFAULT_USERNAME) ?: DEFAULT_USERNAME
        set(value) = sharedPreferences.edit().putString(KEY_USERNAME, value).apply()

    fun clearPrefs() {
        sharedPreferences.edit().clear().apply()
    }
}