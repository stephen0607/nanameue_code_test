package com.example.nanameue_code_test.ui.profile

import androidx.compose.runtime.Composable
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
import com.example.nanameue_code_test.ui.common.ConfirmDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    navController: NavController
) {
    val authState by authViewModel.authState.collectAsState()
    val userInfo = authViewModel.getUserInfo()
    var showSignOutDialog by remember { mutableStateOf(false) }
    LaunchedEffect(authState) {
        if (authState is AuthState.Initial && userInfo == null) {
            viewModel.onSignOutComplete()
        }
    }

    AppScaffold(
        navController = navController,
        title = stringResource(R.string.profile),
        showAppBar = true,
        content = { innerPadding ->
            if (showSignOutDialog) {
                ConfirmDialog(
                    onDismiss = { showSignOutDialog = false },
                    title = stringResource(R.string.confirm_sign_out),
                    message = stringResource(R.string.confirm_sign_out_message),
                    onConfirm = {
                        showSignOutDialog = false
                        authViewModel.signOut()
                    },
                    onCancel = { showSignOutDialog = false }
                )
            }

            ProfileContent(
                displayName = userInfo?.displayName.orEmpty(),
                email = userInfo?.email,
                uid = userInfo?.uid,
                authState = authState,
                showSignOutDialog = showSignOutDialog,
                onSignOutClick = { showSignOutDialog = true },
                onConfirmSignOut = {
                    showSignOutDialog = false
                    authViewModel.signOut()
                },
                onDismissDialog = { showSignOutDialog = false },
                innerPadding = innerPadding
            )

        }
    )
}
