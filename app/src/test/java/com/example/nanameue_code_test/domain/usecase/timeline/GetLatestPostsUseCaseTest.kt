package com.example.nanameue_code_test.domain.usecase.timeline

import com.google.firebase.firestore.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetLatestPostsUseCaseTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var collectionRef: CollectionReference
    private lateinit var query: Query
    private lateinit var querySnapshot: QuerySnapshot
    private lateinit var documentSnapshot: DocumentSnapshot

    private lateinit var useCase: GetLatestPostsUseCase

    @Before
    fun setup() {
        mockkStatic("kotlinx.coroutines.tasks.TasksKt") // enable .await mocking

        firestore = mockk()
        collectionRef = mockk()
        query = mockk()
        querySnapshot = mockk()
        documentSnapshot = mockk()

        every { firestore.collection("posts") } returns collectionRef
        every { collectionRef.orderBy("timestamp", Query.Direction.DESCENDING) } returns query
        every { query.limit(10) } returns query
        every { query.get() } returns mockk()

        useCase = GetLatestPostsUseCase(firestore)
    }

    @Test
    fun `should return post list on success`() = runBlocking {
        val mockPost = Post("John", "Hello world!", null, "user123", 123456789L)

        // Simulate Firestore response
        every { query.get() } returns mockk()
        coEvery { query.get().await() } returns querySnapshot
        every { querySnapshot.documents } returns listOf(documentSnapshot)
        every { documentSnapshot.toObject(Post::class.java) } returns mockPost

        val result = useCase.execute()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("John", result.getOrNull()?.get(0)?.displayName)
    }

    @Test
    fun `should return failure on exception`() = runBlocking {
        coEvery { query.get().await() } throws Exception("Firestore error")

        val result = useCase.execute()

        assertTrue(result.isFailure)
        assertEquals("Firestore error", result.exceptionOrNull()?.message)
    }
}
