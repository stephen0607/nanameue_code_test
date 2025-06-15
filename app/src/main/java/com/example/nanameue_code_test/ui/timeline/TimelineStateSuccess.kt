package com.example.nanameue_code_test.ui.timeline

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.nanameue_code_test.domain.usecase.timeline.Post
import com.example.nanameue_code_test.ui.timeline.post.PostUi

@Composable
fun TimelineStateSuccess(posts: List<Post>, scrollState: ScrollState) {
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        posts.forEach { PostUi(it) }
    }
}