package com.rekognizevote.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rekognizevote.di.AppModule
import com.rekognizevote.ui.screens.auth.AuthViewModel
import com.rekognizevote.ui.screens.camera.CameraViewModel
import com.rekognizevote.ui.screens.polls.PollsViewModel
import com.rekognizevote.ui.screens.vote.VoteViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            AuthViewModel::class.java -> AuthViewModel(AppModule.authRepository) as T
            PollsViewModel::class.java -> PollsViewModel(AppModule.pollRepository) as T
            VoteViewModel::class.java -> VoteViewModel(AppModule.pollRepository, AppModule.voteRepository) as T
            CameraViewModel::class.java -> CameraViewModel(AppModule.voteRepository, AppModule.okHttpClient) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}