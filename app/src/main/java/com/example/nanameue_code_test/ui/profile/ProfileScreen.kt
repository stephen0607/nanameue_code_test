package com.example.nanameue_code_test.ui.profile

import SnackbarController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthState
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.ConfirmDialog
import com.example.nanameue_code_test.ui.common.FullScreenLoading
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(), authViewModel: AuthViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(snackbarHostState, scope) }
    val authState by authViewModel.authState.collectAsState()
    val userInfo = authViewModel.getUserInfo()
    var showSignOutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Initial && authViewModel.getUserInfo() == null) {
            snackbarController.showMessage("Signed Out")
            viewModel.onSignOutComplete()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Profile") }, navigationIcon = {
                IconButton(onClick = { viewModel.navigateToTimeline() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            })
        }) { innerPadding ->
        if (showSignOutDialog) {
            ConfirmDialog({ showSignOutDialog = false },
                "Confirm Sign Out",
                "Are you sure you want to sign out?",
                {
                    showSignOutDialog = false
                    authViewModel.signOut()
                },
                { showSignOutDialog = false })
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (authState) {
                is AuthState.Loading -> {
                    FullScreenLoading()
                    return@Column
                }

                is AuthState.Error -> {
                    AuthFailUi(authState) {
                        // todo: retry logic
                    }
                    return@Column
                }

                else -> {}
            }

            userInfo?.let { user ->
                Text(
                    text = "Welcome ${user.displayName}!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        user.email?.let {
                            Text("Email: $it", style = MaterialTheme.typography.bodyLarge)
                        }
                        Text("UID: ${user.uid}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Box(modifier = Modifier.weight(1f))
                Button(
                    onClick = { showSignOutDialog = true }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Out")
                }
            }
        }
    }
}
