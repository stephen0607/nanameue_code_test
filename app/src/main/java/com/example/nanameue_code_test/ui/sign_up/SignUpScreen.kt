package com.example.nanameue_code_test.ui.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthSuccessUi
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.FieldSpacer
import com.example.nanameue_code_test.ui.common.FullScreenLoading
import com.example.nanameue_code_test.ui.common.SingleLineTextField
import com.example.nanameue_code_test.ui.common.ValidationErrorText
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

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = { Text("Sign Up") }
            )
        }
    ) { paddingValues ->
        when (authState) {
            is AuthState.Success -> AuthSuccessUi { viewModel.navigateToTimeline() }
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
            SingleLineTextField(
                value = uiState.displayName,
                onValueChange = { viewModel.updateDisplayName(it) },
                label = "DisplayName",
                isError = !uiState.isDisplayNameValid
            )
            if (!uiState.isDisplayNameValid) {
                ValidationErrorText("Display Name can't empty")
            }

            FieldSpacer()

            SingleLineTextField(
                value = uiState.email,
                onValueChange = { viewModel.updateEmail(it) },
                label = "Email",
                isError = !uiState.isEmailValid && uiState.email.isNotEmpty()
            )
            if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
                ValidationErrorText("Please enter a valid email")
            }

            FieldSpacer()

            SingleLineTextField(
                value = uiState.password,
                onValueChange = { viewModel.updatePassword(it) },
                label = "Password",
                isError = !uiState.isPasswordValid && uiState.password.isNotEmpty()
            )
            if (!uiState.isPasswordValid && uiState.password.isNotEmpty()) {
                ValidationErrorText("Password must be at least 8 characters")
            }

            FieldSpacer()

            SingleLineTextField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.updateConfirmPassword(it) },
                label = "Confirm Password",
                isError = !uiState.isConfirmPasswordValid && uiState.confirmPassword.isNotEmpty()
            )
            if (!uiState.isConfirmPasswordValid && uiState.confirmPassword.isNotEmpty()) {
                ValidationErrorText("Passwords do not match")
            }

            Spacer(modifier = Modifier.height(Dimensions.paddingMedium))

            Button(
                onClick = {
                    authViewModel.signUp(
                        email = uiState.email,
                        password = uiState.password,
                        displayName = uiState.displayName
                    )
                },
                enabled = uiState.isButtonEnabled
            ) {
                Text("Sign Up")
            }

            FieldSpacer()

            Button(
                onClick = {
                    viewModel.autoFillSignUpForTesting()
                }
            ) {
                Text("Auto Fill for Sign Up (For Testing)")
            }
        }
    }
}