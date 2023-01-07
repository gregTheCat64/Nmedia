package com.example.nmedia.repository.di

import com.example.nmedia.repository.PostRepository
import com.example.nmedia.repository.PostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsPostRepository(impl: PostRepositoryImpl): PostRepository

}