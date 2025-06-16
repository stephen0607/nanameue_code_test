package com.example.nanameue_code_test.ui.create_post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.nanameue_code_test.R
import com.example.nanameue_code_test.domain.usecase.create_post.FakeCreatePostUseCase
import com.example.nanameue_code_test.style.Dimensions
import com.example.nanameue_code_test.ui.auth.AuthViewModel
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.ErrorDialog
import com.example.nanameue_code_test.ui.common.LoadingDialog
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

    LaunchedEffect(uiState) {
        if (uiState is CreatePostUiState.Success) {
            createPostViewModel.createPostSuccessAction()
        }
    }

    AppScaffold(
        navController = navController,
        title = stringResource(R.string.create_new_post),
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState) {
                    is CreatePostUiState.Loading -> LoadingDialog()
                    is CreatePostUiState.Error -> {
                        ErrorDialog((uiState as CreatePostUiState.Error).message) {
                            createPostViewModel.resetUiState()
                        }
                    }

                    is CreatePostUiState.Input -> {
                        val nameToShow = user?.let {
                            when {
                                !it.displayName.isNullOrBlank() -> it.displayName
                                !it.email.isNullOrBlank() -> it.email
                                else -> null
                            }
                        }
                        CreatePostForm(
                            uiState = uiState as CreatePostUiState.Input,
                            viewModel = createPostViewModel,
                            nameToShow = nameToShow,
                            imagePickerLauncher = imagePickerLauncher
                        )
                    }

                    is CreatePostUiState.Success -> {
                        // Navigation will be handled by LaunchedEffect
                    }
                }
            }
        }
    )
}

@Composable
private fun CreatePostForm(
    uiState: CreatePostUiState.Input,
    viewModel: CreatePostViewModel,
    nameToShow: String?,
    imagePickerLauncher: androidx.activity.result.ActivityResultLauncher<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.paddingMedium)
    ) {
        nameToShow?.let { name ->
            Text(
                stringResource(R.string.current_user, name),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(Dimensions.paddingSmall))

        OutlinedTextField(
            value = uiState.postContent,
            onValueChange = viewModel::onPostContentChanged,
            placeholder = { Text(stringResource(R.string.whats_happening)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.paddingSmall))

        uiState.imageUri?.let { uri ->
            SelectedImageWithRemoveButton(uri) {
                viewModel.removeImage()
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
                onClick = { viewModel.createPost() },
                enabled = uiState.isPostButtonEnable
            ) {
                Text(stringResource(R.string.post))
            }
        }
    }
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

@Preview(showBackground = true)
@Composable
fun CreatePostFormPreview() {
    val dummyState = CreatePostUiState.Input(
        postContent = "This is a preview post.",
        imageUri = null,
        isPostButtonEnable = true
    )

    val fakeViewModel = object : CreatePostViewModel(createPostUseCase = FakeCreatePostUseCase()) {}


    val dummyLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { /* no-op */ }

    CreatePostForm(
        uiState = dummyState,
        viewModel = fakeViewModel,
        nameToShow = "fake user",
        imagePickerLauncher = dummyLauncher
    )
}

@Preview(showBackground = true)
@Composable
fun CreatePostFormEmptyPreview() {
    val dummyState = CreatePostUiState.Input(
        postContent = "",
        imageUri = null,
        isPostButtonEnable = false
    )

    val fakeViewModel = object : CreatePostViewModel(createPostUseCase = FakeCreatePostUseCase()) {}


    val dummyLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { /* no-op */ }

    CreatePostForm(
        uiState = dummyState,
        viewModel = fakeViewModel,
        nameToShow = "fake user",
        imagePickerLauncher = dummyLauncher
    )
}