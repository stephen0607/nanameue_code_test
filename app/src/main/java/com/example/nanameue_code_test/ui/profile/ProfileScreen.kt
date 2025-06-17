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
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.ConfirmDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSignOutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUserInfo()
        viewModel.event.collect { event ->
            when (event) {
                is ProfileEvent.Error -> {
                    // Handle error if needed
                }

                else -> {} // Navigation events are handled in NavigationStack
            }
        }
    }



    AppScaffold(
        navController = navController,
        title = stringResource(R.string.profile),
        showAppBar = true,
        content = { innerPadding ->
            ProfileUi(
                displayName = uiState.userInfo?.displayName.orEmpty(),
                email = uiState.userInfo?.email,
                uid = uiState.userInfo?.uid,
                onSignOutClick = { showSignOutDialog = true },
                innerPadding = innerPadding
            )
            if (showSignOutDialog) {
                ConfirmDialog(
                    onDismiss = { showSignOutDialog = false },
                    title = stringResource(R.string.confirm_sign_out),
                    message = stringResource(R.string.confirm_sign_out_message),
                    onConfirm = {
                        showSignOutDialog = false
                        viewModel.signOut()
                    },
                    onCancel = { showSignOutDialog = false }
                )
            }
        }
    )
}
