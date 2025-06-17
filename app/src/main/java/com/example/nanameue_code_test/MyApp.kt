package com.example.nanameue_code_test

import android.app.Application
import com.example.nanameue_code_test.di.appModule
import com.example.nanameue_code_test.di.firebaseModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(firebaseModule, appModule))
        }
    }
}