package com.example.nanameue_code_test.ui.auth

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AuthFailUi(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text("Error") },
        text = { Text(message) }
    )
}

@Composable
fun AuthSuccessUi(
    message: String = "Your account has been created.",
    onClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onClick) {
                Text("Go to timeline")
            }
        },
        title = { Text("Registration Successful") },
        text = { Text(message) }
    )
}
