package com.example.nanameue_code_test.di

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.example.nanameue_code_test.data.repository.FirebaseAuthRepositoryImpl
import com.example.nanameue_code_test.domain.usecase.auth.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val firebaseModule = module {
    // Firebase core services
    single {
        // Ensure Firebase is initialized
        if (FirebaseApp.getApps(androidContext()).isEmpty()) {
            FirebaseApp.initializeApp(androidContext())
        }
        FirebaseAuth.getInstance()
    }
    single { FirebaseStorage.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // Repository
    single<FirebaseAuthRepository> { FirebaseAuthRepositoryImpl(get()) }

    // Use Cases: Auth
    single { GetAuthStateUseCase(get()) }
    single { SignInUseCase(get()) }
    single { SignUpUseCase(get()) }
    single { SignOutUseCase(get()) }

    // Use Cases: User Info
    single { UpdateDisplayNameUseCase(get()) }
    single { GetUserInfoUseCase() }
}
