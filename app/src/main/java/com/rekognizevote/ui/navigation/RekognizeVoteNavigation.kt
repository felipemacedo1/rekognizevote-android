package com.rekognizevote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.rekognizevote.ui.ViewModelFactory
import com.rekognizevote.ui.screens.auth.AuthViewModel
import com.rekognizevote.ui.screens.auth.LoginScreen
import com.rekognizevote.ui.screens.auth.RegisterScreen
import com.rekognizevote.ui.screens.camera.CameraScreen
import com.rekognizevote.ui.screens.camera.CameraViewModel
import com.rekognizevote.ui.screens.onboarding.SplashScreen
import com.rekognizevote.ui.screens.polls.HomeScreen
import com.rekognizevote.ui.screens.polls.PollDetailsScreen
import com.rekognizevote.ui.screens.polls.PollsViewModel
import com.rekognizevote.ui.screens.results.ResultsScreen
import com.rekognizevote.ui.screens.vote.VoteScreen
import com.rekognizevote.ui.screens.vote.VoteViewModel

@Composable
fun RekognizeVoteNavigation(navController: NavHostController) {
    val viewModelFactory = ViewModelFactory()
    
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToOnboarding = { navController.navigate("login") },
                onNavigateToHome = { navController.navigate("home") }
            )
        }
        
        composable("login") {
            val viewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToHome = { navController.navigate("home") },
                viewModel = viewModel
            )
        }
        
        composable("register") {
            val viewModel: AuthViewModel = viewModel(factory = viewModelFactory)
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToHome = { navController.navigate("home") },
                viewModel = viewModel
            )
        }
        
        composable("home") {
            val viewModel: PollsViewModel = viewModel(factory = viewModelFactory)
            HomeScreen(
                onNavigateToPollDetails = { pollId: String ->
                    navController.navigate("poll_details/$pollId")
                },
                onNavigateToResults = { pollId: String ->
                    navController.navigate("results/$pollId")
                },
                viewModel = viewModel
            )
        }
        
        composable("poll_details/{pollId}") { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            val viewModel: PollsViewModel = viewModel(factory = viewModelFactory)
            PollDetailsScreen(
                pollId = pollId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVote = { pollId: String ->
                    navController.navigate("vote/$pollId")
                },
                viewModel = viewModel
            )
        }
        
        composable("vote/{pollId}") { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            val viewModel: VoteViewModel = viewModel(factory = viewModelFactory)
            VoteScreen(
                pollId = pollId,
                onNavigateBack = { navController.popBackStack() },
                onVoteSuccess = { navController.navigate("results/$pollId") },
                viewModel = viewModel
            )
        }
        
        composable("results/{pollId}") { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            val viewModel: PollsViewModel = viewModel(factory = viewModelFactory)
            ResultsScreen(
                pollId = pollId,
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        
        composable("camera") {
            val viewModel: CameraViewModel = viewModel(factory = viewModelFactory)
            CameraScreen(
                onImageCaptured = { imageUri: String ->
                    navController.popBackStack()
                },
                onDismiss = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}