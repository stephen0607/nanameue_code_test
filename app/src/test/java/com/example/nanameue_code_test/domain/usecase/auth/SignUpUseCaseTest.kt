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

class SignUpUseCaseTest {
    private lateinit var useCase: SignUpUseCase
    private lateinit var mockAuthRepository: FirebaseAuthRepository
    private lateinit var mockFirebaseUser: FirebaseUser

    @Before
    fun setup() {
        mockAuthRepository = mockk()
        mockFirebaseUser = mockk()
        useCase = SignUpUseCase(mockAuthRepository)
    }

    @Test
    fun `when sign up is successful, return success result with user`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        coEvery { mockAuthRepository.signUp(email, password) } returns Result.success(mockFirebaseUser)

        // Act
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(mockFirebaseUser, result.getOrNull())
    }

    @Test
    fun `when sign up fails, return failure result`() = runBlocking {
        // Arrange
        val email = "test@example.com"
        val password = "weakpassword"
        val exception = Exception("Password is too weak")
        coEvery { mockAuthRepository.signUp(email, password) } returns Result.failure(exception)

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

    @Test
    fun `when email is invalid, return failure result`() = runBlocking {
        // Arrange
        val email = "invalid-email"
        val password = "password123"

        // Act
        val result = useCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Invalid email format") == true)
    }
} 