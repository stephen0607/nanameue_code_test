package com.example.nanameue_code_test.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nanameue_code_test.R
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.common.ConfirmDialog
import com.example.nanameue_code_test.ui.common.ErrorDialog
import com.example.nanameue_code_test.ui.common.FullScreenLoading

@Composable
fun ProfileContent(
    displayName: String,
    email: String?,
    uid: String?,
    authState: AuthState,
    showSignOutDialog: Boolean,
    onSignOutClick: () -> Unit,
    onConfirmSignOut: () -> Unit,
    onDismissDialog: () -> Unit,
    innerPadding: PaddingValues
) {
    if (showSignOutDialog) {
        ConfirmDialog(
            onDismiss = onDismissDialog,
            title = stringResource(R.string.confirm_sign_out),
            message = stringResource(R.string.confirm_sign_out_message),
            onConfirm = onConfirmSignOut,
            onCancel = onDismissDialog
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
                ErrorDialog(authState.message) {
                    // TODO: Add retry logic
                }
                return@Column
            }

            else -> {}
        }

        Text(
            text = stringResource(R.string.welcome_user, displayName),
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
                email?.let {
                    Text(
                        text = stringResource(R.string.email) + ": $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Text(
                    text = "UID: $uid",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Box(modifier = Modifier.weight(1f))

        Button(
            onClick = onSignOutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.sign_out))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    ProfileContent(
        displayName = "User",
        email = "user@example.com",
        uid = "UID-1234567890",
        authState = AuthState.Initial,
        showSignOutDialog = false,
        onSignOutClick = {},
        onConfirmSignOut = {},
        onDismissDialog = {},
        innerPadding = PaddingValues(16.dp)
    )
}