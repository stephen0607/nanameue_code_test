package com.example.nanameue_code_test.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.example.nanameue_code_test.ui.common.FieldSpacer
import com.example.nanameue_code_test.ui.common.SingleLineTextField
import com.example.nanameue_code_test.ui.common.ValidationErrorText

@Composable
fun LoginFormUi(
    email: String,
    password: String,
    isEmailValid: Boolean,
    isPasswordValid: Boolean,
    isButtonEnabled: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onAutoFillClick: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(Dimensions.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SingleLineTextField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.email),
            isError = !isEmailValid && email.isNotEmpty()
        )
        if (!isEmailValid && email.isNotEmpty()) {
            ValidationErrorText(stringResource(R.string.error_invalid_email))
        }

        FieldSpacer()

        SingleLineTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.password),
            isError = !isPasswordValid && password.isNotEmpty()
        )
        if (!isPasswordValid && password.isNotEmpty()) {
            ValidationErrorText(stringResource(R.string.error_password_length))
        }

        FieldSpacer()

        Text(
            text = stringResource(R.string.click_to_sign_up),
            modifier = Modifier.clickable(onClick = onSignUpClick),
            style = MaterialTheme.typography.bodyMedium
        )

        FieldSpacer()

        Button(onClick = onSignInClick, enabled = isButtonEnabled) {
            Text(stringResource(R.string.login))
        }

        FieldSpacer()

        Button(onClick = onAutoFillClick) {
            Text("Input for testing, need to remove !!!!!!!!!!!")
            Text(stringResource(R.string.login))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormUiPreview() {
    LoginFormUi(
        email = "user@example.com",
        password = "password123",
        isEmailValid = true,
        isPasswordValid = true,
        isButtonEnabled = true,
        onEmailChange = {},
        onPasswordChange = {},
        onSignInClick = {},
        onSignUpClick = {},
        onAutoFillClick = {},
        paddingValues = PaddingValues(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginFormUiEmptyInputPreview() {
    LoginFormUi(
        email = "",
        password = "",
        isEmailValid = true,
        isPasswordValid = true,
        isButtonEnabled = false,
        onEmailChange = {},
        onPasswordChange = {},
        onSignInClick = {},
        onSignUpClick = {},
        onAutoFillClick = {},
        paddingValues = PaddingValues(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginFormUiErrorInputPreview() {
    LoginFormUi(
        email = "abc",
        password = "abc",
        isEmailValid = false,
        isPasswordValid = false,
        isButtonEnabled = false,
        onEmailChange = {},
        onPasswordChange = {},
        onSignInClick = {},
        onSignUpClick = {},
        onAutoFillClick = {},
        paddingValues = PaddingValues(16.dp)
    )
}

