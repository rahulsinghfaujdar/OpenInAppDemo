package com.openinapp.extension

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preference @Inject constructor(@ApplicationContext private val context: Context) {

    private val preference: SharedPreferences = context.getSharedPreferences("openinapp_preference", Context.MODE_PRIVATE)

    init {
        saveApiToken("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI")
    }

    fun saveApiToken(apiToken: String) {
        val editor = preference.edit()
        editor.putString("token", apiToken)
        editor.apply()
    }

    fun getApiToken():String = preference.getString("token", "NO_TOKEN")!!
}