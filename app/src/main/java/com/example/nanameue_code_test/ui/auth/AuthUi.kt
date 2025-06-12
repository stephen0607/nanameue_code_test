package com.example.nanameue_code_test.ui.auth

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun AuthFailUi(authState: AuthState, onDismiss: () -> Unit) {
    val errorMessage = (authState as AuthState.Error).message
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text("Error") },
        text = { Text(errorMessage) }
    )
}


@Composable
fun AuthSuccessUi(onClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onClick) {
                Text("Go to timeline")
            }
        },
        title = { Text("Registration Successful") },
        text = { Text("Your account has been created.") }
    )
}