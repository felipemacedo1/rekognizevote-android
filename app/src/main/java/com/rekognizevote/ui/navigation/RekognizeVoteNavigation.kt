package com.rekognizevote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rekognizevote.ui.screens.auth.LoginScreen
import com.rekognizevote.ui.screens.auth.RegisterScreen
import com.rekognizevote.ui.screens.onboarding.OnboardingScreen
import com.rekognizevote.ui.screens.onboarding.SplashScreen
import com.rekognizevote.ui.screens.polls.HomeScreen
import com.rekognizevote.ui.screens.polls.PollDetailsScreen
import com.rekognizevote.ui.screens.results.ResultsScreen
import com.rekognizevote.ui.screens.vote.VoteScreen

@Composable
fun RekognizeVoteNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        modifier = modifier
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Routes.Onboarding.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Routes.Onboarding.route) {
            OnboardingScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.Login.route)
                }
            )
        }
        
        composable(Routes.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                viewModel = hiltViewModel()
            )
        }
        
        composable(Routes.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Register.route) { inclusive = true }
                    }
                },
                viewModel = hiltViewModel()
            )
        }
        
        composable(Routes.Home.route) {
            HomeScreen(
                onNavigateToPollDetails = { pollId ->
                    navController.navigate(Routes.PollDetails.createRoute(pollId))
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.Profile.route)
                },
                viewModel = hiltViewModel()
            )
        }
        
        composable(Routes.PollDetails.route) { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            PollDetailsScreen(
                pollId = pollId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToVote = { pollId ->
                    navController.navigate(Routes.Vote.createRoute(pollId))
                },
                onNavigateToResults = { pollId ->
                    navController.navigate(Routes.Results.createRoute(pollId))
                },
                viewModel = hiltViewModel()
            )
        }
        
        composable(Routes.Vote.route) { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            VoteScreen(
                pollId = pollId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onVoteSuccess = {
                    navController.popBackStack()
                },
                viewModel = hiltViewModel()
            )
        }
        
        composable(Routes.Results.route) { backStackEntry ->
            val pollId = backStackEntry.arguments?.getString("pollId") ?: ""
            ResultsScreen(
                pollId = pollId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                viewModel = hiltViewModel()
            )
        }
    }
}