package com.example.nanameue_code_test.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.nanameue_code_test.R


@Composable
fun ConfirmDialog(
    onDismiss: () -> Unit,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
        })
}


@Preview(showBackground = true)
@Composable
fun ConfirmDialogPreview() {
    ConfirmDialog(onDismiss = {},
        title = "Delete Post",
        message = "Are you sure you want to delete this post?",
        onConfirm = {},
        onCancel = {})
}