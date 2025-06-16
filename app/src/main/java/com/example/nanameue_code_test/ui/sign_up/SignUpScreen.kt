package com.example.nanameue_code_test.ui.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.nanameue_code_test.R
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.ErrorDialog
import com.example.nanameue_code_test.ui.common.FieldSpacer
import com.example.nanameue_code_test.ui.common.LoadingDialog
import com.example.nanameue_code_test.ui.common.SingleLineTextField
import com.example.nanameue_code_test.ui.common.ValidationErrorText
import org.koin.androidx.compose.koinViewModel

@Preview(showBackground = true)
@Composable
fun SignUpFormPreview() {
    SignUpForm(
        uiState = SignUpUiState.Input(
            displayName = "John Doe",
            email = "john@example.com",
            password = "password123",
            confirmPassword = "password123",
            isEmailValid = true,
            isPasswordValid = true,
            isConfirmPasswordValid = true,
            isButtonEnabled = true
        ),
        viewModel = SignUpViewModel(),
        onSubmit = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpFormWithErrorsPreview() {
    SignUpForm(
        uiState = SignUpUiState.Input(
            displayName = "John Doe",
            email = "invalid-email",
            password = "short",
            confirmPassword = "different",
            isEmailValid = false,
            isPasswordValid = false,
            isConfirmPasswordValid = false,
            isButtonEnabled = false
        ),
        viewModel = SignUpViewModel(),
        onSubmit = {}
    )
}

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val registrationFailedMsg = stringResource(R.string.registration_failed)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetUiState()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SignUpUiEvent.Success -> viewModel.navigateToTimeline()
                is SignUpUiEvent.Error -> {
                    errorMessage = event.message
                }
            }
        }
    }

    AppScaffold(
        navController = navController,
        title = stringResource(R.string.sign_up),
        content = { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                when (uiState) {
                    is SignUpUiState.Input -> {
                        val state = uiState as SignUpUiState.Input
                        if (state.isLoading) {
                            LoadingDialog()
                        }
                        SignUpForm(state, viewModel) {
                            viewModel.signUpStart()
                            authViewModel.signUp(
                                email = state.email,
                                password = state.password,
                                displayName = state.displayName,
                                onResult = { result ->
                                    result.onSuccess { viewModel.signUpSuccess() }
                                        .onFailure {
                                            viewModel.signUpFailed(
                                                it.message ?: registrationFailedMsg
                                            )
                                        }
                                }
                            )
                        }
                    }

                    is SignUpUiState.Error -> TODO()
                    SignUpUiState.Success -> TODO()
                }

                errorMessage?.let { message ->
                    ErrorDialog(message) {
                        errorMessage = null
                        viewModel.onDialogDismissAction()
                        authViewModel.resetAuthState()
                    }
                }
            }
        }
    )
}

@Composable
fun SignUpForm(
    uiState: SignUpUiState.Input,
    viewModel: SignUpViewModel,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier.padding(Dimensions.paddingMedium),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SingleLineTextField(
            value = uiState.displayName,
            onValueChange = { viewModel.updateDisplayName(it) },
            label = stringResource(R.string.display_name)
        )
        FieldSpacer()

        SingleLineTextField(
            value = uiState.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = stringResource(R.string.email),
            isError = !uiState.isEmailValid && uiState.email.isNotEmpty()
        )
        if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
            ValidationErrorText(stringResource(R.string.error_invalid_email))
        }

        FieldSpacer()

        SingleLineTextField(
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = stringResource(R.string.password),
            isError = !uiState.isPasswordValid && uiState.password.isNotEmpty()
        )
        if (!uiState.isPasswordValid && uiState.password.isNotEmpty()) {
            ValidationErrorText(stringResource(R.string.error_password_length))
        }

        FieldSpacer()

        SingleLineTextField(
            value = uiState.confirmPassword,
            onValueChange = { viewModel.updateConfirmPassword(it) },
            label = stringResource(R.string.confirm_password),
            isError = !uiState.isConfirmPasswordValid && uiState.confirmPassword.isNotEmpty()
        )
        if (!uiState.isConfirmPasswordValid && uiState.confirmPassword.isNotEmpty()) {
            ValidationErrorText(stringResource(R.string.error_passwords_not_match))
        }

        Box(modifier = Modifier.weight(1f))

        Button(
            onClick = onSubmit,
            enabled = uiState.isButtonEnabled && !uiState.isLoading
        ) {
            Text(stringResource(R.string.sign_up))
        }

        Button(onClick = { viewModel.autoFillSignUpForTesting() }) {
            Text("Auto Fill for Sign Up (Testing)") // optional to localize
        }
    }
}
