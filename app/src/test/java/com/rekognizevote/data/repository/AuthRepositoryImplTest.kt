package com.rekognizevote.data.repository

import com.rekognizevote.core.Result
import com.rekognizevote.data.local.SecureStorage
import com.rekognizevote.data.remote.ApiService
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRepositoryImplTest {

    @Mock
    private lateinit var apiService: ApiService
    
    @Mock
    private lateinit var secureStorage: SecureStorage
    
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = AuthRepositoryImpl(apiService, secureStorage)
    }

    @Test
    fun `login with valid credentials returns success`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        
        val result = repository.login(email, password)
        
        assertTrue(result is Result.Success)
        verify(secureStorage).saveToken("mock_access_token_123")
        verify(secureStorage).saveUserEmail(email)
    }

    @Test
    fun `login with empty credentials returns error`() = runTest {
        val email = ""
        val password = ""
        
        val result = repository.login(email, password)
        
        assertTrue(result is Result.Error)
        assertEquals("Email e senha são obrigatórios", result.exception.message)
    }

    @Test
    fun `isLoggedIn returns storage value`() = runTest {
        whenever(secureStorage.isLoggedIn()).thenReturn(true)
        
        val result = repository.isLoggedIn()
        
        assertTrue(result)
        verify(secureStorage).isLoggedIn()
    }
}