package com.example.nmedia.dao

import com.example.nmedia.db.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    fun providePostDao(db: AppDb): PostDao = db.postDao()

    @Provides
    fun providePostRemoteKeyDao(db: AppDb): PostRemoteKeyDao = db.postRemoteKeyDao()
}