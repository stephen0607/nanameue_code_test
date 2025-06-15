package com.example.nanameue_code_test.domain.usecase.auth

import com.example.nanameue_code_test.data.repository.FirebaseAuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SignOutUseCaseTest {
    private lateinit var useCase: SignOutUseCase
    private lateinit var mockAuthRepository: FirebaseAuthRepository

    @Before
    fun setup() {
        mockAuthRepository = mockk()
        useCase = SignOutUseCase(mockAuthRepository)
    }

    @Test
    fun `when sign out is successful, return success result`() = runBlocking {
        // Arrange
        coEvery { mockAuthRepository.signOut() } returns Result.success(Unit)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `when sign out fails, return failure result`() = runBlocking {
        // Arrange
        val exception = Exception("Failed to sign out")
        coEvery { mockAuthRepository.signOut() } returns Result.failure(exception)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
} 