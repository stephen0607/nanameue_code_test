package com.example.nanameue_code_test


sealed class Screen(val route: String) {
    data object Splash : Screen("Splash_screen")
    data object Login : Screen("Login_screen")
    data object SignUp : Screen("Sign_up_screen")
    data object Timeline : Screen("Timeline_screen")
    data object Profile : Screen("Profile_screen")
    data object CreatePost : Screen("Create_post_screen")
}