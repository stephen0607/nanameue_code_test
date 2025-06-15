package com.example.nanameue_code_test.domain.usecase.auth

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetAuthStateUseCaseTest {
    private lateinit var useCase: GetAuthStateUseCase
    private lateinit var mockAuthRepository: FirebaseAuthRepository
    private lateinit var mockFirebaseUser: FirebaseUser

    @Before
    fun setup() {
        mockAuthRepository = mockk()
        mockFirebaseUser = mockk()
        useCase = GetAuthStateUseCase(mockAuthRepository)
    }

    @Test
    fun `when user is authenticated, return user`() = runBlocking {
        // Arrange
        every { mockAuthRepository.getAuthState() } returns flowOf(mockFirebaseUser)

        // Act
        val result = useCase().first()

        // Assert
        assertEquals(mockFirebaseUser, result)
    }

    @Test
    fun `when user is not authenticated, return null`() = runBlocking {
        // Arrange
        every { mockAuthRepository.getAuthState() } returns flowOf(null)

        // Act
        val result = useCase().first()

        // Assert
        assertNull(result)
    }
} 