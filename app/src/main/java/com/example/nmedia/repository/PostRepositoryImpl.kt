package com.example.nmedia.repository


import androidx.paging.*
import kotlinx.coroutines.flow.*
import com.example.nmedia.appError.*
import com.example.nmedia.api.ApiService
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.dao.PostDao
import com.example.nmedia.dao.PostRemoteKeyDao
import com.example.nmedia.db.AppDb
import com.example.nmedia.dto.*
import com.example.nmedia.entity.PostEntity
import com.example.nmedia.entity.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: ApiService,
    private val appAuth: AppAuth,
    postRemoteKeyDao: PostRemoteKeyDao,
    appDb: AppDb
    ) : PostRepository {
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<FeedItem>> = Pager(
        config = PagingConfig(pageSize = 5, enablePlaceholders = false),
        pagingSourceFactory = {dao.pagingSource()},
        remoteMediator = PostRemoteMediator(apiService, dao, postRemoteKeyDao, appDb)
    ).flow
        .map { pagingData ->
            pagingData.map { postEntity ->
            postEntity.toDto() }
                .insertSeparators { previous, _ ->
                if (previous?.id?.rem(5) == 0L) {
                    Ad(Random.nextLong(), "figma.jpg")
                } else {
                    null
            }
            } }
    

    override suspend fun getAll() {
        try {
            val response = apiService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            for (i in body) {
                i.savedOnServer = true
            }
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

//    override suspend fun updateShownStatus() {
//        dao.updateShownStatus()
//    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            delay(50_000L)
            val response = apiService.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            for (i in body) {
//                i.toShow = false
//            }
            dao.insert(body.toEntity())
            emit(body.size)
        }
    }
        .catch { e -> throw AppError.from(e) }
        .flowOn(Dispatchers.Default)


    override suspend fun save(post: Post, upload: MediaUpload?) {
        try {
            val postWithAttachment = upload?.let {
                upload(it)
            }?.let {
                post.copy(
                    attachment = Attachment(it.id, AttachmentType.IMAGE),
                    savedOnServer = false
                )
            }
            val response = apiService.save(postWithAttachment ?: post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            body.savedOnServer = true
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    //    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
//        try {
//            val file = uploadFile(upload)
//            val postWithAttachment = post.copy(
//                attachment = Attachment(file.id, AttachmentType.IMAGE),
//                toShow = true,
//                savedOnServer = true
//            )
//            save(postWithAttachment)
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
//    }
    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val part = MultipartBody.Part.createFormData(
                "file",
                upload.file.name,
                upload.file.asRequestBody()
            )
            val response = apiService.uploadPic(part)
            return response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun likeById(id: Long) {
        dao.likeById(id)
        try {
            val response = apiService.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
           // dao.insert(PostEntity.fromDto(body).apply { toShow = true })
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikeById(id: Long) {
        dao.likeById(id)
        try {
            val response = apiService.dislikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        dao.removeById(id)
        try {
            val response = apiService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }

    override suspend fun updateUser(login: String, pass: String) {
        try {
            val response = apiService.updateUser(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body()?: throw ApiError(response.code(), response.message())
            val id = body.id
            val token = body.token
            appAuth.setAuth(id,token)

        } catch (e: IOException) {
            throw NetworkError
        }catch (e: Exception) {
            println(e)
            throw UnknownError

        }
    }

    override suspend fun registerUser(login: String, pass: String, name: String) {
        try {
            val response = apiService.registerUser(login, pass, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body()?: throw ApiError(response.code(), response.message())
            val id = body.id
            val token = body.token
            appAuth.setAuth(id,token)

        } catch (e: IOException) {
            throw NetworkError
        }catch (e: Exception) {
            println(e)
            throw UnknownError

        }
    }

}