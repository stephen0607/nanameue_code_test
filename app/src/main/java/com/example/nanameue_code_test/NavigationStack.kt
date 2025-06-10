package com.example.nanameue_code_test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nanameue_code_test.ui.login.LoginEvent
import com.example.nanameue_code_test.ui.login.LoginScreen
import com.example.nanameue_code_test.ui.login.LoginViewModel
import com.example.nanameue_code_test.ui.sign_up.SignUpEvent
import com.example.nanameue_code_test.ui.sign_up.SignUpScreen
import com.example.nanameue_code_test.ui.sign_up.SignUpViewModel
import com.example.nanameue_code_test.ui.timeline.TimelineScreen
import kotlinx.coroutines.flow.merge
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationStack() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = koinViewModel()
    val signUpViewModel: SignUpViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        merge(
            loginViewModel.navigationEvent,
            signUpViewModel.navigationEvent
        ).collect { event ->
            when (event) {
                is LoginEvent.NavigateToSignUp -> {
                    navController.navigate(Screen.SignUp.route)
                }

                is LoginEvent.NavigateToTimeline -> {
                    navController.navigate(Screen.Timeline.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }

                is SignUpEvent.NavigateBack -> {
                    navController.navigateUp()
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(viewModel = loginViewModel)
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(viewModel = signUpViewModel)
        }
        composable(route = Screen.Timeline.route) {
            TimelineScreen()
        }
    }
}