package com.rekognizevote.ui.screens.auth

import com.rekognizevote.core.Result
import com.rekognizevote.domain.model.AuthResponse
import com.rekognizevote.domain.model.User
import com.rekognizevote.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @Mock
    private lateinit var authRepository: AuthRepository
    
    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with valid credentials updates state to success`() = runTest {
        val mockUser = User("1", "Test User", "test@example.com")
        val mockResponse = AuthResponse("token", "refresh", mockUser)
        whenever(authRepository.login("test@example.com", "password"))
            .thenReturn(Result.Success(mockResponse))

        viewModel.login("test@example.com", "password")

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isAuthenticated)
        assertEquals(mockResponse, state.authResponse)
    }

    @Test
    fun `login with invalid credentials updates state to error`() = runTest {
        whenever(authRepository.login("", ""))
            .thenReturn(Result.Error(Exception("Email e senha são obrigatórios")))

        viewModel.login("", "")

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.isAuthenticated)
        assertEquals("Email e senha são obrigatórios", state.error)
    }
}