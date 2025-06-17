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
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.ErrorDialog
import com.example.nanameue_code_test.ui.common.LoadingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(), navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        onDispose { viewModel.resetUiState() }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.Error -> {
                    errorMessage = event.message
                }

                else -> {} // Navigation events are handled in NavigationStack
            }
        }
    }

    AppScaffold(navController = navController,
        title = stringResource(R.string.login),
        content = { paddingValues ->
            if (uiState.isLoading) {
                LoadingDialog()
            }

            LoginFormUi(
                email = uiState.email,
                password = uiState.password,
                isEmailValid = uiState.isEmailValid,
                isPasswordValid = uiState.isPasswordValid,
                isButtonEnabled = uiState.isButtonEnabled,
                onEmailChange = viewModel::updateEmail,
                onPasswordChange = viewModel::updatePassword,
                onSignInClick = viewModel::login,
                onSignUpClick = viewModel::signUp,
                onAutoFillClick = viewModel::autoFillForTesting,
                paddingValues = paddingValues
            )

            errorMessage?.let { message ->
                ErrorDialog(message) {
                    errorMessage = null
                    viewModel.onDialogDismissAction()
                }
            }
        })
}