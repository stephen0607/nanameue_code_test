package com.example.nanameue_code_test.domain.usecase.auth

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SignInUseCaseTest {
    private lateinit var useCase: SignInUseCase
    private lateinit var mockAuthRepository: FirebaseAuthRepository
    private lateinit var mockFirebaseUser: FirebaseUser

    @Before
    fun setup() {
        mockAuthRepository = mockk()
        mockFirebaseUser = mockk()
        useCase = SignInUseCase(mockAuthRepository)
    }

    @Test
    fun `when sign in is successful, return success result with user`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        coEvery { mockAuthRepository.signIn(email, password) } returns Result.success(mockFirebaseUser)

        // Act
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockFirebaseUser, result.getOrNull())
    }

    @Test
    fun `when sign in fails, return failure result`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "wrongpassword"
        val exception = Exception("Invalid credentials")
        coEvery { mockAuthRepository.signIn(email, password) } returns Result.failure(exception)

        // Act
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `when email is empty, return failure result`() = runBlocking {
        // Arrange
        val email = ""
        val password = "password123"

        // Act
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Email") == true)
    }

    @Test
    fun `when password is empty, return failure result`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = ""

        // Act
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Password") == true)
    }
} 