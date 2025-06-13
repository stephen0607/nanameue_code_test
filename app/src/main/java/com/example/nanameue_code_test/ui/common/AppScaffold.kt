package com.example.nanameue_code_test.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun AppScaffold(
    navController: NavController,
    title: String,
    showAppBar: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
) {
    Scaffold(
        topBar = {
            if (showAppBar) {
                CommonTopAppBar(navController = navController, title = title, actions = actions)
            }
        },
        content = content,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton
    )
}
