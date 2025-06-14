package com.example.nanameue_code_test.ui.timeline.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.nanameue_code_test.domain.usecase.timeline.Post
import com.example.nanameue_code_test.style.Dimensions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview
@Composable
fun PostUi(post: Post = fakePost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = Dimensions.paddingSmall),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (post.displayName.isNotBlank()) {
                    Text(
                        text = post.displayName + " ",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = "@" + post.userId.slice(0..8),
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            if (post.content.isNotBlank()) {
                Text(text = post.content, fontSize = 16.sp)
            }
            post.imageUrl?.let { safeImageUrl ->
                Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
                NetworkImage(safeImageUrl)
            }
            Spacer(modifier = Modifier.height(Dimensions.paddingSmall))
            Text(
                text = formatTimestamp(post.timestamp), fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
fun NetworkImage(imageUrl: String) {
    var isPortrait by remember { mutableStateOf<Boolean?>(null) }

    SubcomposeAsyncImage(model = ImageRequest.Builder(LocalContext.current).data(imageUrl).build(),
        contentDescription = null,
        loading = {
            Box(
                Modifier
                    .height(200.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
            ) {}
        },
        success = { success ->
            val painter = success.painter as? AsyncImagePainter
            val intrinsicSize = painter?.intrinsicSize
            isPortrait = intrinsicSize?.let { it.height > it.width }

            if (isPortrait == true) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    this@SubcomposeAsyncImage.SubcomposeAsyncImageContent(
                        modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.Fit
                    )
                }
            } else {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth
                )
            }
        })
}

// Fake post for preview
val fakePost = Post(
    displayName = "user1",
    timestamp = 1749724533875,
    userId = "uid1",
    content = "Hello World!",
    imageUrl = "https://img.freepik.com/free-photo/portrait-young-woman-with-natural-make-up_23-2149084942.jpg?semt=ais_hybrid&w=740"
)

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
    return format.format(date)
}
