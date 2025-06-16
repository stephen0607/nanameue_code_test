package com.example.nanameue_code_test.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.nanameue_code_test.R
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.ErrorDialog
import com.example.nanameue_code_test.ui.common.LoadingDialog
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
                    ErrorDialog((authState as AuthState.Error).message) {
                        showDialog = false
                        authViewModel.resetAuthState()
                    }
                }

                is AuthState.Loading -> LoadingDialog()
                else -> {}
            }

            LoginFormUi(
                email = uiState.email,
                password = uiState.password,
                isEmailValid = uiState.isEmailValid,
                isPasswordValid = uiState.isPasswordValid,
                isButtonEnabled = uiState.isButtonEnabled,
                onEmailChange = viewModel::updateEmail,
                onPasswordChange = viewModel::updatePassword,
                onSignInClick = {
                    authViewModel.signIn(
                        email = uiState.email,
                        password = uiState.password
                    )
                },
                onSignUpClick = viewModel::signUp,
                onAutoFillClick = viewModel::autoFillForTesting,
                paddingValues = paddingValues
            )
        }
    )
}
