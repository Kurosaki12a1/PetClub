package com.kien.petclub.domain.usecase

import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.usecase.auth.SignInUseCase
import com.kien.petclub.domain.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        signInUseCase = SignInUseCase(authRepository)
    }

    @Test
    fun `signIn with valid credentials returns success`() = runTest {
        val email = "bkukuro@gmail.com"
        val password = "AAbc123"
        // Giả sử đây là một instance của FirebaseUser.
        val mockUser: FirebaseUser = mockk()

        // Cấu hình MockK
        coEvery { authRepository.signIn(email, password) } returns flow {
            emit(Resource.success(mockUser))
        }

        // Chạy use case và lấy kết quả
        val result = signInUseCase(email, password).first()
        assertTrue(result is Resource.Success && result.value == mockUser)
    }

    @Test
    fun `signIn with invalid email format returns failure`() = runTest {
        val email = "invalid"
        val password = "password123"

        // Chạy use case và lấy kết quả
        val result = signInUseCase(email, password).first()

        assertTrue(result is Resource.Failure)
    }
}