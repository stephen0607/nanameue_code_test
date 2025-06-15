package com.example.nanameue_code_test.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nanameue_code_test.R
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { viewModel.resetUiState() }
    }

    LaunchedEffect(authState) {
        showDialog = authState is AuthState.Error
        if (authState is AuthState.Success) {
            viewModel.login()
        }
    }

    AppScaffold(
        navController = navController,
        title = stringResource(R.string.login),
        content = { paddingValues ->
            when (authState) {
                is AuthState.Error -> if (showDialog) {
                    AuthFailUi((authState as AuthState.Error).message) {
                        showDialog = false
                        authViewModel.resetAuthState()
                    }
                }

                is AuthState.Loading -> LoadingDialog()
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
                    value = uiState.email,
                    onValueChange = viewModel::updateEmail,
                    label = stringResource(R.string.email),
                    isError = !uiState.isEmailValid && uiState.email.isNotEmpty()
                )
                if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
                    ValidationErrorText(stringResource(R.string.error_invalid_email))
                }

                FieldSpacer()

                SingleLineTextField(
                    value = uiState.password,
                    onValueChange = viewModel::updatePassword,
                    label = stringResource(R.string.password),
                    isError = !uiState.isPasswordValid && uiState.password.isNotEmpty()
                )
                if (!uiState.isPasswordValid && uiState.password.isNotEmpty()) {
                    ValidationErrorText(stringResource(R.string.error_password_length))
                }

                FieldSpacer()

                Text(
                    stringResource(R.string.click_to_sign_up),
                    modifier = Modifier.clickable { viewModel.signUp() },
                    style = MaterialTheme.typography.bodyMedium
                )

                FieldSpacer()

                Button(
                    onClick = {
                        authViewModel.signIn(
                            email = uiState.email,
                            password = uiState.password
                        )
                    },
                    enabled = uiState.isButtonEnabled
                ) {
                    Text("Login")
                }

                FieldSpacer()

                Button(onClick = viewModel::autoFillForTesting) {
                    Text("Input for testing, need to remove !!!!!!!!!!!")
                    Text(stringResource(R.string.login))
                }
            }
        }
    )
}