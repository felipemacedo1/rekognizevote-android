package com.rekognizevote.ui.navigation

sealed class Routes(val route: String) {
    data object Splash : Routes("splash")
    data object Onboarding : Routes("onboarding")
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object Home : Routes("home")
    data object PollDetails : Routes("poll_details/{pollId}") {
        fun createRoute(pollId: String) = "poll_details/$pollId"
    }
    data object Vote : Routes("vote/{pollId}") {
        fun createRoute(pollId: String) = "vote/$pollId"
    }
    data object Camera : Routes("camera/{pollId}/{candidateId}") {
        fun createRoute(pollId: String, candidateId: String) = "camera/$pollId/$candidateId"
    }
    data object Results : Routes("results/{pollId}") {
        fun createRoute(pollId: String) = "results/$pollId"
    }
    data object Profile : Routes("profile")
}