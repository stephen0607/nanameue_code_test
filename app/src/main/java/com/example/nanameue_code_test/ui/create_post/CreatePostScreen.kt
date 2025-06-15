package com.example.nanameue_code_test.ui.create_post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nanameue_code_test.R
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthFailUi
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.FullScreenLoading
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePostScreen(
    createPostViewModel: CreatePostViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    navController: NavController
) {
    val uiState by createPostViewModel.uiState.collectAsState()
    val user = authViewModel.getUserInfo()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        createPostViewModel.onImageSelected(uri)
    }

    DisposableEffect(Unit) {
        onDispose {
            createPostViewModel.resetUiState()
        }
    }

    AppScaffold(
        navController = navController,
        title = stringResource(R.string.create_new_post),
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (uiState.status) {
                    CreatePostStatus.LOADING -> FullScreenLoading()

                    CreatePostStatus.ERROR -> {
                        uiState.errorMessage?.let { msg ->
                            AuthFailUi(msg) {
                                createPostViewModel.resetUiState()
                            }
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(Dimensions.paddingMedium)
                        ) {
                            user?.let {
                                val nameToShow = when {
                                    !it.displayName.isNullOrBlank() -> it.displayName
                                    !it.email.isNullOrBlank() -> it.email
                                    else -> null
                                }
                                nameToShow?.let { name ->
                                    Text(
                                        stringResource(R.string.current_user, name),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))

                            OutlinedTextField(
                                value = uiState.postContent,
                                onValueChange = createPostViewModel::onPostContentChanged,
                                placeholder = { Text(stringResource(R.string.whats_happening)) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))

                            uiState.imageUri?.let { uri ->
                                SelectedImageWithRemoveButton(uri) {
                                    createPostViewModel.removeImage()
                                }
                                Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                                    Icon(
                                        imageVector = Icons.Filled.AddCircle,
                                        contentDescription = "Add image",
                                    )
                                }

                                Button(
                                    onClick = { createPostViewModel.createPost() },
                                    enabled = uiState.isPostButtonEnable
                                ) {
                                    Text(stringResource(R.string.post))
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SelectedImageWithRemoveButton(
    imageUri: Uri,
    onRemoveImage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = onRemoveImage,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Dimensions.paddingSmall)
                .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove Image",
            )
        }
    }
}
