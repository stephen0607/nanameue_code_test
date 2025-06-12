package com.example.nanameue_code_test.domain.usecase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class CreatePostUseCase(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore
) {
    suspend fun execute(title: String, imageUri: Uri?): Result<Unit> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val id = currentUser.uid
        val displayName = currentUser.uid

        return try {
            val imageUrl = imageUri?.let { uploadImage(it, id) }
            savePostToFirestore(displayName, title, imageUrl, id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadImage(uri: Uri, userId: String): String {
        val fileName = "posts/$userId/${System.currentTimeMillis()}"
        val ref = storage.reference.child(fileName)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    private suspend fun savePostToFirestore(
        displayName: String,
        content: String,
        imageUrl: String?,
        userId: String
    ) {
        val post = mapOf(
            "displayName" to displayName,
            "content" to content,
            "imageUrl" to imageUrl,
            "userId" to userId,
            "timestamp" to System.currentTimeMillis()
        )
        firestore.collection("posts").add(post).await()
    }
}
