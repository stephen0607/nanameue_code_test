package com.example.nanameue_code_test

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nanameue_code_test.ui.login.LoginScreen
import com.example.nanameue_code_test.ui.timeline.TimelineScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen()
        }

        composable(route = Screen.Timeline.route) {
            TimelineScreen()
        }
    }
}