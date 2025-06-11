package com.example.nanameue_code_test.ui.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthSuccessUi
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    SignUpScreenUi(viewModel, uiState, authViewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenUi(
    viewModel: SignUpViewModel,
    uiState: SignUpUiState,
    authViewModel: AuthViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    Scaffold(topBar = {
        TopAppBar(navigationIcon = {
            IconButton(onClick = { viewModel.navigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }, title = { Text("Sign Up") })
    }) { paddingValues ->


        when (authState) {
            is AuthState.Success -> AuthSuccessUi { viewModel.navigateBack() }
            is AuthState.Error -> AuthFailUi(authState) { authViewModel.resetAuthState() }
            is AuthState.Loading -> FullScreenLoading()
            else -> {}
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimensions.paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                isError = !uiState.isEmailValid && uiState.email.isNotEmpty()
            )
            if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
                Text(
                    text = "Please enter a valid email",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = Dimensions.paddingSmall)
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                isError = !uiState.isPasswordValid && uiState.password.isNotEmpty()
            )
            if (!uiState.isPasswordValid && uiState.password.isNotEmpty()) {
                Text(
                    text = "Password must be at least 8 characters",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = Dimensions.paddingSmall)
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                isError = !uiState.isConfirmPasswordValid && uiState.confirmPassword.isNotEmpty()
            )
            if (!uiState.isConfirmPasswordValid && uiState.confirmPassword.isNotEmpty()) {
                Text(
                    text = "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = Dimensions.paddingSmall)
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingMedium))
            Button(
                onClick = {
                    authViewModel.signUp(
                        email = uiState.email,
                        password = uiState.password
                    )
                }, enabled = uiState.isButtonEnabled
            ) {
                Text("Sign Up")
            }
        }
    }
}

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}