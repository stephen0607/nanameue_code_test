package com.example.nanameue_code_test.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.nanameue_code_test.R
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.ErrorDialog
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.ConfirmDialog
import com.example.nanameue_code_test.ui.common.FullScreenLoading
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(Dimensions.paddingMedium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (authState) {
                    is AuthState.Loading -> {
                        FullScreenLoading()
                        return@Column
                    }

                    is AuthState.Error -> {
                        ErrorDialog((authState as AuthState.Error).message) {
                            // TODO: Add retry logic
                        }
                        return@Column
                    }

                    else -> {}
                }

                userInfo?.let { user ->
                    Text(
                        text = stringResource(R.string.welcome_user,    "123"),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = Dimensions.paddingMedium)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimensions.paddingSmall),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(Dimensions.paddingMedium)) {
                            user.email?.let {
                                Text(
                                    text = stringResource(R.string.email) + ": $it",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Text(
                                text = "UID: ${user.uid}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Box(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { showSignOutDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.sign_out))
                    }
                }
            }
        }
    )
}
