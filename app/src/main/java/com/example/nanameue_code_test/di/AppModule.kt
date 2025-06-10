package com.example.nanameue_code_test.di

import com.example.nanameue_code_test.ui.login.LoginViewModel
import com.example.nanameue_code_test.ui.sign_up.SignUpViewModel
import com.example.nanameue_code_test.ui.timeline.TimelineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LoginViewModel() }
    viewModel { SignUpViewModel() }
    viewModel { TimelineViewModel() }
}