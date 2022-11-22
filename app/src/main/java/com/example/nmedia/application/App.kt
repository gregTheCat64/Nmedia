package com.example.nmedia.application

import android.app.Application
import com.example.nmedia.auth.AppAuth

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppAuth.initAuth(this)
    }
}