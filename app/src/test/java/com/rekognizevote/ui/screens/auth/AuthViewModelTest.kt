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
    fun `login with valid credentials should succeed`() = runTest {
        val mockUser = User("1", "Test User", "test@example.com")
        val mockAuthResponse = AuthResponse("token", "refresh", mockUser)
        
        whenever(authRepository.login("test@example.com", "password123"))
            .thenReturn(Result.Success(mockAuthResponse))
        
        viewModel.login("test@example.com", "password123")
        
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isAuthenticated)
        assertEquals(mockAuthResponse, state.authResponse)
    }
    
    @Test
    fun `login with invalid email should show validation error`() = runTest {
        viewModel.login("invalid-email", "password123")
        
        val state = viewModel.uiState.value
        assertEquals("Email inválido", state.emailError)
        assertFalse(state.isAuthenticated)
    }
    
    @Test
    fun `register with mismatched passwords should show error`() = runTest {
        viewModel.register("Test User", "test@example.com", "password123", "different")
        
        val state = viewModel.uiState.value
        assertEquals("Senhas não coincidem", state.confirmPasswordError)
        assertFalse(state.isAuthenticated)
    }
}