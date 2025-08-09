package com.rekognizevote.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rekognizevote.ui.screens.auth.AuthViewModel
import com.rekognizevote.ui.screens.auth.LoginScreen
import com.rekognizevote.ui.theme.RekognizeVoteTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysCorrectElements() {
        val mockViewModel = mock(AuthViewModel::class.java)
        
        composeTestRule.setContent {
            RekognizeVoteTheme {
                LoginScreen(
                    onNavigateToRegister = {},
                    onNavigateToHome = {},
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule.onNodeWithText("RekognizeVote").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Senha").assertIsDisplayed()
        composeTestRule.onNodeWithText("Entrar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Criar conta").assertIsDisplayed()
    }

    @Test
    fun loginScreen_emailInput_acceptsText() {
        val mockViewModel = mock(AuthViewModel::class.java)
        
        composeTestRule.setContent {
            RekognizeVoteTheme {
                LoginScreen(
                    onNavigateToRegister = {},
                    onNavigateToHome = {},
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule.onNodeWithText("Email")
            .performTextInput("test@example.com")
        
        composeTestRule.onNodeWithText("test@example.com").assertIsDisplayed()
    }
}