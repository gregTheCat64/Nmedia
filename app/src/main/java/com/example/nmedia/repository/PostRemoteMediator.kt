package com.example.nmedia.repository

import androidx.paging.*
import androidx.room.withTransaction
import com.example.nmedia.api.ApiService
import com.example.nmedia.appError.ApiError
import com.example.nmedia.dao.PostDao
import com.example.nmedia.dao.PostRemoteKeyDao
import com.example.nmedia.db.AppDb
import com.example.nmedia.entity.PostEntity
import com.example.nmedia.entity.PostRemoteKeyEntity

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator (
    private val service: ApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val appDb: AppDb
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PostEntity>): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    //service.getLatest(state.config.pageSize)
                    val id = postRemoteKeyDao.max()
                    if (id != null){
                        service.getAfter(id, state.config.pageSize)
                    } else  service.getLatest(state.config.pageSize)


                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(false)
                }
                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    service.getBefore(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(
                response.code(),
                response.message(),
            )

            appDb.withTransaction {
                when(loadType) {
                    LoadType.REFRESH -> {
                        postRemoteKeyDao.clear()
                        postRemoteKeyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.AFTER,
                                    body.first().id
                                ),
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.BEFORE,
                                    body.last().id
                                )
                            )
                        )
                        //postDao.clear()

                    }
                    LoadType.PREPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.AFTER,
                                body.first().id
                            )
                        )
                    }
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                PostRemoteKeyEntity.KeyType.BEFORE,
                                body.last().id
                            )
                        )
                    }
                }
                body.map {
                    it.savedOnServer = true
                }
                postDao.insert(body.map(PostEntity::fromDto))
            }

            return MediatorResult.Success(body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

}