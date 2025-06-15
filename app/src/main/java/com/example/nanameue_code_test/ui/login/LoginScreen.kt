package com.example.nanameue_code_test.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.FieldSpacer
import com.example.nanameue_code_test.ui.common.LoadingDialog
import com.example.nanameue_code_test.ui.common.SingleLineTextField
import com.example.nanameue_code_test.ui.common.ValidationErrorText
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
        title = "Login",
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
                    label = "Email",
                    isError = !uiState.isEmailValid && uiState.email.isNotEmpty()
                )
                if (!uiState.isEmailValid && uiState.email.isNotEmpty()) {
                    ValidationErrorText("Please enter a valid email")
                }

                FieldSpacer()

                SingleLineTextField(
                    value = uiState.password,
                    onValueChange = viewModel::updatePassword,
                    label = "Password",
                    isError = !uiState.isPasswordValid && uiState.password.isNotEmpty()
                )
                if (!uiState.isPasswordValid && uiState.password.isNotEmpty()) {
                    ValidationErrorText("Password must be at least 8 characters")
                }

                FieldSpacer()

                Text(
                    "Click here to sign up",
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
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = rememberNavController())
}
