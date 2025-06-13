package com.example.nanameue_code_test.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    onClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(
                onClick = onClick
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissClick) {
                Text("Cancel")
            }
        })
}

