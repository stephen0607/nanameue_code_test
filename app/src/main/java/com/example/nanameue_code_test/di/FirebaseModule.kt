package com.example.nanameue_code_test.di

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.example.nanameue_code_test.data.repository.FirebaseAuthRepositoryImpl
import com.example.nanameue_code_test.domain.usecase.auth.SignInUseCase
import com.example.nanameue_code_test.domain.usecase.auth.SignOutUseCase
import com.example.nanameue_code_test.domain.usecase.auth.SignUpUseCase
import com.example.nanameue_code_test.domain.usecase.create_post.CreatePostUseCase
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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

    single { FirebaseStorage.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Repository
    single<FirebaseAuthRepository> {
        FirebaseAuthRepositoryImpl(get())
    }

    // ViewModel
    viewModel { AuthViewModel(get(), get(), get(), get()) }

    single { CreatePostUseCase(get(), get(), get()) }
    single { SignInUseCase(get()) }
    single { SignOutUseCase(get()) }
    single { SignUpUseCase(get()) }

}