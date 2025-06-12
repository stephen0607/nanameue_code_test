package com.example.nanameue_code_test.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nanameue_code_test.style.Dimensions

@Composable
fun SingleLineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError
    )
}

@Composable
fun ValidationErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(start = Dimensions.paddingSmall)
    )
}

@Composable
fun FieldSpacer() {
    Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
}