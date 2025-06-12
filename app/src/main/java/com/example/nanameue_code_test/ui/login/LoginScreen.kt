package com.example.nanameue_code_test.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.FieldSpacer
import com.example.nanameue_code_test.ui.common.SingleLineTextField
import com.example.nanameue_code_test.ui.common.ValidationErrorText
import com.example.nanameue_code_test.ui.sign_up.FullScreenLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(), authViewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        showDialog = authState is AuthState.Error
        if (authState is AuthState.Success) {
            viewModel.login()
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Login") })
    }) { paddingValues ->

        if (showDialog && authState is AuthState.Error) {
            AuthFailUi(authState) {
                showDialog = false
                authViewModel.resetAuthState()
            }
        }

        if (authState is AuthState.Loading) {
            FullScreenLoading()
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

            Text(
                "Click here to sign up", modifier = Modifier.clickable {
                    viewModel.signUp()
                }, style = MaterialTheme.typography.bodyMedium
            )

            FieldSpacer()

            Button(
                onClick = {
                    authViewModel.signIn(
                        email = uiState.email, password = uiState.password
                    )
                }, enabled = uiState.isButtonEnabled
            ) {
                Text("Login")
            }

            FieldSpacer()

            Button(onClick = {
                viewModel.autoFillForTesting()
            }) {
                Text("Input for testing, need to remove !!!!!!!!!!!")
            }
        }
    }
}
