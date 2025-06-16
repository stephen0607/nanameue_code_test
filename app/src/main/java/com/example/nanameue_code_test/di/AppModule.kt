package com.example.nanameue_code_test.di

import com.example.nanameue_code_test.domain.usecase.create_post.CreatePostUseCase
import com.example.nanameue_code_test.domain.usecase.create_post.ICreatePostUseCase
import com.example.nanameue_code_test.domain.usecase.timeline.GetLatestPostsUseCase
import com.example.nanameue_code_test.ui.create_post.CreatePostViewModel
import com.example.nanameue_code_test.ui.login.LoginViewModel
import com.example.nanameue_code_test.ui.profile.ProfileViewModel
import com.example.nanameue_code_test.ui.sign_up.SignUpViewModel
import com.example.nanameue_code_test.ui.splash.SplashViewModel
import com.example.nanameue_code_test.ui.timeline.TimelineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // viewModel
    viewModel { LoginViewModel() }
    viewModel { SignUpViewModel() }
    viewModel { TimelineViewModel(get()) }
    viewModel { ProfileViewModel() }
    viewModel { CreatePostViewModel(get()) }
    viewModel { SplashViewModel(get()) }

    // Use Cases
    single { GetLatestPostsUseCase(get()) }
    single<ICreatePostUseCase> { CreatePostUseCase(get(), get(), get()) }
}