package com.openinapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var appContext: App
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

}