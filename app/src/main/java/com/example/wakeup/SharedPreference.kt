package com.example.wakeup

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPreference(private val context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(APP_NAME, MODE_PRIVATE)

    fun putUserName(name: String) {
        preferences.edit().putString(USER_NAME, name).apply()
    }

    fun getUserName() = preferences.getString(USER_NAME, DEFAULT)


    companion object {

        private const val APP_NAME = "COMPOSITION_OF_NUMBER"
        private const val USER_NAME = "USER_NAME"
        private const val DEFAULT = "DEFAULT"
    }
}