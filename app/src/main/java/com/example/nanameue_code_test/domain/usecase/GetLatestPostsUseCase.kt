package com.example.nanameue_code_test.domain.usecase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Post(
    val displayName: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val userId: String = "",
    val timestamp: Long = 0L
)

class GetLatestPostsUseCase(
    private val firestore: FirebaseFirestore
) {
    suspend fun execute(): Result<List<Post>> {
        return try {
            val snapshot = firestore
                .collection("posts")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Post::class.java)
            }

            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
