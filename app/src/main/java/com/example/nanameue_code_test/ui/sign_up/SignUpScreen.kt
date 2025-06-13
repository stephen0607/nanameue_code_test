package com.example.nanameue_code_test.ui.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthSuccessUi
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.FieldSpacer
import com.example.nanameue_code_test.ui.common.FullScreenLoading
import com.example.nanameue_code_test.ui.common.SingleLineTextField
import com.example.nanameue_code_test.ui.common.ValidationErrorText
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    DisposableEffect(Unit) {
        onDispose { viewModel.resetUiState() }
    }

    AppScaffold(
        navController = navController,
        title = "Sign Up",
        content = { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                when (uiState.status) {
                    SignUpStatus.LOADING -> FullScreenLoading()
                    SignUpStatus.SUCCESS -> AuthSuccessUi { viewModel.navigateToTimeline() }
                    SignUpStatus.ERROR -> AuthFailUi(
                        message = uiState.errorMessage ?: "Sign up failed",
                        onDismiss = { viewModel.resetStatus() }
                    )

                    SignUpStatus.INPUT -> SignUpForm(uiState, viewModel) {
                        viewModel.signUpStart()
                        authViewModel.signUp(
                            email = uiState.email,
                            password = uiState.password,
                            displayName = uiState.displayName,
                            onResult = { result ->
                                result.onSuccess { viewModel.signUpSuccess() }
                                    .onFailure {
                                        viewModel.signUpFailed(
                                            it.message ?: "Sign up failed"
                                        )
                                    }
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SignUpForm(
    uiState: SignUpUiState,
    viewModel: SignUpViewModel,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SingleLineTextField(
            value = uiState.displayName,
            onValueChange = { viewModel.updateDisplayName(it) },
            label = "DisplayName",
        )
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

        Box(modifier = Modifier.weight(1f))

        Button(onClick = onSubmit, enabled = uiState.isButtonEnabled) {
            Text("Sign Up")
        }

        FieldSpacer()

        Button(onClick = { viewModel.autoFillSignUpForTesting() }) {
            Text("Auto Fill for Sign Up (Testing)")
        }
    }
}
