package com.example.nanameue_code_test.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.nanameue_code_test.R

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        },
        title = { Text(stringResource(R.string.authentication_failed)) },
        text = { Text(message) })
}

@Preview(showBackground = true)
@Composable
fun ErrorDialogPreview() {
    ErrorDialog(message = "Invalid email or password.", onDismiss = {})
}