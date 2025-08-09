package com.rekognizevote.di

import com.rekognizevote.data.repository.AuthRepositoryImpl
import com.rekognizevote.data.repository.PollRepositoryImpl
import com.rekognizevote.data.repository.VoteRepositoryImpl
import com.rekognizevote.domain.repository.AuthRepository
import com.rekognizevote.domain.repository.PollRepository
import com.rekognizevote.domain.repository.VoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindPollRepository(
        pollRepositoryImpl: PollRepositoryImpl
    ): PollRepository
    
    @Binds
    @Singleton
    abstract fun bindVoteRepository(
        voteRepositoryImpl: VoteRepositoryImpl
    ): VoteRepository
}