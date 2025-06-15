package com.example.nanameue_code_test.ui.auth

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.nanameue_code_test.R

@Composable
fun AuthFailUi(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        },
        title = { Text(stringResource(R.string.authentication_failed)) },
        text = { Text(message) }
    )
}

@Composable
fun AuthSuccessUi(
    message: String = stringResource(R.string.account_created),
    onClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onClick) {
                Text(stringResource(R.string.go_to_timeline))
            }
        },
        title = { Text(stringResource(R.string.registration_successful)) },
        text = { Text(message) }
    )
}
