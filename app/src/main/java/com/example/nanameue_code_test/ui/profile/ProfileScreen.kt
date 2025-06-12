package com.example.nanameue_code_test.ui.profile

import SnackbarController
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.sign_up.FullScreenLoading
import org.koin.androidx.compose.koinViewModel

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(snackbarHostState, scope) }
    val authState by authViewModel.authState.collectAsState()

    val userInfo = authViewModel.getUserInfo()

    LaunchedEffect(authState) {
        if (authState is AuthState.Initial) {
            snackbarController.showMessage("Sign Out")
            viewModel.onSignOutComplete()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(navigationIcon = {
                IconButton(onClick = { viewModel.navigateToTimeline() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }, title = { Text("Profile") })
        }
    ) { innerPadding ->
        Column(
            Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (authState) {
                is AuthState.Error -> {
                    AuthFailUi(authState) {
                        // todo add authState button onclick action
                    }
                }

                is AuthState.Loading -> {
                    FullScreenLoading()
                }

                else -> {}
            }
            userInfo?.let { user ->
                user.email?.let { email ->
                    Text("Email: $email")
                }
                Text("UID: ${userInfo.uid}")
            }
            Button(
                onClick = {
                    authViewModel.signOut()
                }
            ) {
                Text("Sign Out")
            }
        }
    }
}
