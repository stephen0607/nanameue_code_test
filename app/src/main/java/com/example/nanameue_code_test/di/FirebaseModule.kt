package com.example.nanameue_code_test.di

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.example.nanameue_code_test.data.repository.FirebaseAuthRepositoryImpl
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val firebaseModule = module {
    // Firebase Auth - ensure Firebase is initialized
    single { 
        // Make sure Firebase is initialized
        if (!FirebaseApp.getApps(androidContext()).any()) {
            FirebaseApp.initializeApp(androidContext())
        }
        FirebaseAuth.getInstance()
    }

    // Repository
    single<FirebaseAuthRepository> { 
        FirebaseAuthRepositoryImpl(get())
    }

    // ViewModel
    viewModel { AuthViewModel(get()) }
} 