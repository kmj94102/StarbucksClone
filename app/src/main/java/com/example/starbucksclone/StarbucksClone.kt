package com.example.starbucksclone

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StarbucksClone : Application() {

    companion object {
        private lateinit var application: StarbucksClone
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}