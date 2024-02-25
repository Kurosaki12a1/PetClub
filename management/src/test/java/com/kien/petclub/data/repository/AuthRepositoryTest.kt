package com.kien.petclub.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.utils.mockTask
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    private lateinit var auth: FirebaseAuth

    private lateinit var repo: AuthRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        auth = mockk(relaxed = true)
        repo = AuthRepositoryImpl(auth, testDispatcher)
    }

    @Test
    fun `signIn with valid credentials emits success`(): Unit = runTest(testDispatcher) {
        val email = "bkukuro@gmail.com"
        val password = "123456"
        val mockUser = mockk<FirebaseUser>()
        val mockTask = mockTask<AuthResult>()
        val mockAuthResult = mockk<AuthResult>()
        every { auth.signInWithEmailAndPassword(any(), any()) } returns mockTask
        every { mockTask.result } returns mockAuthResult
        every { mockAuthResult.user } returns mockUser
        val result = repo.signIn(email, password).toList()
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Success && (result.last() as Resource.Success<FirebaseUser?>).value == mockUser)
    }

    @Test
    fun `signIn with invalid credentials emits no exist`(): Unit = runTest(testDispatcher) {
        val email = "abcxyz@gmail.com"
        val password = "123456"
        val mockTask = mockTask<AuthResult>()
        val mockAuthResult = mockk<AuthResult>()
        every {
            auth.signInWithEmailAndPassword(any(), any())
        } returns mockTask
        every { mockTask.result } returns mockAuthResult
        every { mockAuthResult.user } returns null
        val result = repo.signIn(email, password).toList()
        assertTrue(result.first() is Resource.Loading)
        assertTrue(
            result.last() is Resource.Failure && (result.last() as Resource.Failure).error.message.equals(
                "User is not exist"
            )
        )
    }

    @Test
    fun `signIn with invalid credentials emits failure`(): Unit = runTest(testDispatcher) {
        val email = "test@gmail.com"
        val password = "123456"
        every {
            auth.signInWithEmailAndPassword(any(), any())
        } throws Exception("Invalid credentials")
        val result = repo.signIn(email, password).toList()
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Failure)
    }

    @Test
    fun `signOut emits success`(): Unit = runTest(testDispatcher) {
        val mockTask = mockk<Unit>()
        every { auth.signOut() } returns mockTask
        val result = repo.signOut().toList()
        assertTrue(result.first() is Resource.Success)
    }

    @Test
    fun `signOut emits failure`(): Unit = runTest(testDispatcher) {
        every { auth.signOut() } throws Exception("Sign out failed")
        val result = repo.signOut().toList()
        assertTrue(
            result.first() is Resource.Failure && (result.first() as Resource.Failure).error.message.equals(
                "Sign out failed"
            )
        )
    }

    @Test
    fun `recoverPassword emits success`(): Unit = runTest(testDispatcher) {
        val email = "bkukuro@gmail.com"
        val mockTask = mockTask<Void>()
        val mockResult = mockk<Void>()
        every { auth.sendPasswordResetEmail(any()) } returns mockTask
        every { mockTask.result } returns mockResult
        val result = repo.recoverPassword(email).toList()
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Success)
    }

    @Test
    fun `recoverPassword emits failure`(): Unit = runTest(testDispatcher) {
        val email = "test_gmail.com"
        every { auth.sendPasswordResetEmail(any()) } throws FirebaseAuthInvalidUserException(
            "email",
            "No user found with this email."
        )
        val result = repo.recoverPassword(email).toList()
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Failure)
    }

    @Test
    fun `signUp with valid credentials emits success`(): Unit = runTest(testDispatcher) {
        val email = "bkukuro@gmail.com"
        val password = "123456"
        val mockUser = mockk<FirebaseUser>()
        val mockTask = mockTask<AuthResult>()
        val mockAuthResult = mockk<AuthResult>()
        every { auth.createUserWithEmailAndPassword(any(), any()) } returns mockTask
        every { mockTask.result } returns mockAuthResult
        every { mockAuthResult.user } returns mockUser
        val result = repo.signUp(email, password).toList()
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Success && (result.last() as Resource.Success<FirebaseUser?>).value == mockUser)
    }

    @Test
    fun `signUp with invalid credentials emits failure`(): Unit = runTest(testDispatcher) {
        val email = "ExistedEmail@gmail.com"
        val password = "123456"
        val mockTask = mockTask<AuthResult>()
        every { auth.createUserWithEmailAndPassword(any(), any()) } returns mockTask
        every { mockTask.result } throws FirebaseAuthUserCollisionException(
            "email",
            "User already exists."
        )
        val result = repo.signUp(email, password).toList()
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Failure)
    }

    @Test
    fun `signUp with invalid credentials emits failure2`(): Unit = runTest(testDispatcher) {
        val email = "test_gmail.com"
        val password = "123"
        every { auth.createUserWithEmailAndPassword(any(), any()) } throws Exception("Invalid credentials")
        val result = repo.signUp(email, password).toList()
        assertTrue(result.first() is Resource.Loading)
        assertTrue(result.last() is Resource.Failure)
    }

    @Test
    fun `isSignedIn emits true`(): Unit = runTest(testDispatcher) {
        val mockUser = mockk<FirebaseUser>()
        every { auth.currentUser } returns mockUser
        val result = repo.isSignedIn().toList()
        assertTrue(result.first())
    }

    @Test
    fun `isSignedIn emits false`(): Unit = runTest(testDispatcher) {
        every { auth.currentUser } returns null
        val result = repo.isSignedIn().toList()
        assertTrue(!result.first())
    }
}