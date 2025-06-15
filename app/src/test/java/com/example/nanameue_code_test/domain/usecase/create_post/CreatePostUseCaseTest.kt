package com.example.nanameue_code_test.domain.usecase.create_post

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreatePostUseCaseTest {

    private lateinit var useCase: CreatePostUseCase
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockUser: FirebaseUser
    private lateinit var mockStorage: FirebaseStorage
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockStorageRef: StorageReference
    private lateinit var mockUploadTask: UploadTask
    private lateinit var mockUploadSnapshot: UploadTask.TaskSnapshot
    private lateinit var mockDownloadUri: Uri
    private lateinit var mockCollectionRef: CollectionReference
    private lateinit var mockDocumentRef: DocumentReference

    @Before
    fun setup() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        mockAuth = mockk()
        mockUser = mockk()
        mockStorage = mockk()
        mockFirestore = mockk()
        mockStorageRef = mockk()
        mockUploadTask = mockk()
        mockUploadSnapshot = mockk()
        mockDownloadUri = mockk()
        mockCollectionRef = mockk()
        mockDocumentRef = mockk()

        // Firebase Auth
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "testUserId"
        every { mockUser.displayName } returns "Test User"

        // Firebase Storage
        every { mockStorage.reference } returns mockStorageRef
        every { mockStorageRef.child(any()) } returns mockStorageRef
        every { mockStorageRef.putFile(any()) } returns mockUploadTask
        coEvery { mockUploadTask.await() } returns mockUploadSnapshot
        every { mockStorageRef.downloadUrl } returns mockk()
        coEvery { mockStorageRef.downloadUrl.await() } returns mockDownloadUri
        every { mockDownloadUri.toString() } returns "https://example.com/image.jpg"

        // Firestore
        every { mockFirestore.collection("posts") } returns mockCollectionRef
        every { mockCollectionRef.add(any()) } returns mockk()
        coEvery { mockCollectionRef.add(any()).await() } returns mockDocumentRef

        // Create use case
        useCase = CreatePostUseCase(mockAuth, mockStorage, mockFirestore)
    }

    @Test
    fun `should return failure when user is not logged in`() = runBlocking {
        every { mockAuth.currentUser } returns null

        val result = useCase.execute("Test title", null)

        assertTrue(result.isFailure)
        assertEquals("User not logged in", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should succeed when creating post without image`() = runBlocking {
        val result = useCase.execute("Test post no image", null)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `should succeed when creating post with image`() = runBlocking {
        val imageUri = mockk<Uri>()
        val result = useCase.execute("Test post with image", imageUri)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `should fail when image upload fails`() = runBlocking {
        val imageUri = mockk<Uri>()
        every { mockStorageRef.putFile(imageUri) } returns mockUploadTask
        coEvery { mockUploadTask.await() } throws Exception("upload failed")

        val result = useCase.execute("Failing upload", imageUri)

        assertTrue(result.isFailure)
        assertEquals("upload failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should fail when firestore add fails`() = runBlocking {
        coEvery { mockCollectionRef.add(any()).await() } throws Exception("firestore error")

        val result = useCase.execute("Failing Firestore", null)

        assertTrue(result.isFailure)
        assertEquals("firestore error", result.exceptionOrNull()?.message)
    }
}
