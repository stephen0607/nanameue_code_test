package com.example.nanameue_code_test.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    navController: NavController, title: String, actions: @Composable RowScope.() -> Unit = {}
) {
    val canGoBack = navController.previousBackStackEntry != null
    println("stephennn pre ${navController.previousBackStackEntry}")
    TopAppBar(title = { Text(title) }, navigationIcon = {
        if (canGoBack) {
            run {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        } else null
    }, actions = actions
    )
}
