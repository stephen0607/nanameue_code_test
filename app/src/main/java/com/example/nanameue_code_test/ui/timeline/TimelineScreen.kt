package com.example.nanameue_code_test.ui.timeline

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nanameue_code_test.ui.common.AppScaffold
import com.example.nanameue_code_test.ui.common.FullScreenLoading
import com.example.nanameue_code_test.ui.timeline.post.PostUi
import org.koin.androidx.compose.koinViewModel


@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel = koinViewModel(), navController: NavController
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTimeline()
    }

    AppScaffold(navController = navController, title = "Timeline", actions = {
        IconButton(onClick = { viewModel.navigateToProfile() }) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Go to Profile",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }, content = { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is TimelineUiState.NoPost -> {
                    NoPostUi()
                }

                is TimelineUiState.Loading -> {
                    FullScreenLoading()
                }

                is TimelineUiState.Success -> {
                    val posts = (uiState as TimelineUiState.Success).posts
                    Column(
                        modifier = Modifier.verticalScroll(scrollState)
                    ) {
                        posts.forEach { PostUi(it) }
                    }
                }

                is TimelineUiState.Error -> {
                    val msg = (uiState as TimelineUiState.Error).message
                    Text("Error: $msg", color = Color.Red)
                }
            }
        }

    },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.navigateToCreatePost() }, modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Create, contentDescription = "Add")
            }
        })
}