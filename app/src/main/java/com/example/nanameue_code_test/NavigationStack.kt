package com.example.nanameue_code_test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.create_post.CreatePostEvent
import com.example.nanameue_code_test.ui.create_post.CreatePostScreen
import com.example.nanameue_code_test.ui.create_post.CreatePostViewModel
import com.example.nanameue_code_test.ui.login.LoginEvent
import com.example.nanameue_code_test.ui.login.LoginScreen
import com.example.nanameue_code_test.ui.login.LoginViewModel
import com.example.nanameue_code_test.ui.profile.ProfileEvent
import com.example.nanameue_code_test.ui.profile.ProfileScreen
import com.example.nanameue_code_test.ui.profile.ProfileViewModel
import com.example.nanameue_code_test.ui.sign_up.SignUpEvent
import com.example.nanameue_code_test.ui.sign_up.SignUpScreen
import com.example.nanameue_code_test.ui.sign_up.SignUpViewModel
import com.example.nanameue_code_test.ui.timeline.TimelineEvent
import com.example.nanameue_code_test.ui.timeline.TimelineScreen
import com.example.nanameue_code_test.ui.timeline.TimelineViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationStack() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = koinViewModel()
    val signUpViewModel: SignUpViewModel = koinViewModel()
    val timelineViewModel: TimelineViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val createPostViewModel: CreatePostViewModel = koinViewModel()
    val authViewModel: AuthViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        merge(
            loginViewModel.navigationEvent,
            signUpViewModel.navigationEvent,
            profileViewModel.navigationEvent,
            timelineViewModel.navigationEvent,
            createPostViewModel.navigationEvent
        ).collectLatest { event ->
            when (event) {
                is LoginEvent.NavigateToSignUp -> {
                    navController.navigate(Screen.SignUp.route)
                }

                is ProfileEvent.NavigateToTimeline,
                LoginEvent.NavigateToTimeline,
                SignUpEvent.NavigateToTimeline,
                CreatePostEvent.NavigateToTimeline
                    -> {
                    navController.navigate(Screen.Timeline.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }

                is ProfileEvent.NavigateToLogin -> {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }

                is TimelineEvent.NavigateToProfile -> {
                    navController.navigate(Screen.Profile.route)
                }

                is TimelineEvent.NavigateToCreatePost -> {
                    navController.navigate(Screen.CreatePost.route)
                }

                is SignUpEvent.NavigateBack -> {
                    navController.navigateUp()
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(loginViewModel, authViewModel)
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(signUpViewModel, authViewModel)
        }
        composable(route = Screen.Timeline.route) {
            TimelineScreen(timelineViewModel)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(profileViewModel, authViewModel)
        }
        composable(route = Screen.CreatePost.route) {
            CreatePostScreen(createPostViewModel, authViewModel)
        }

    }
}

open class NavigationEvent {}
