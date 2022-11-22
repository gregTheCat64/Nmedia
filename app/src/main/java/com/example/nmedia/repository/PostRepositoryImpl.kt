package com.example.nmedia.repository

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import com.example.nmedia.appError.*
import com.example.nmedia.api.PostApi
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.dao.PostDao
import com.example.nmedia.dto.*
import com.example.nmedia.entity.PostEntity
import com.example.nmedia.entity.toDto
import com.example.nmedia.entity.toEntity
import com.example.nmedia.model.PhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException


class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll()
        .map(List<PostEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = PostApi.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            for (i in body) {
                i.toShow = true
                i.savedOnServer = true
            }
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun updateShownStatus() {
        dao.updateShownStatus()
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            delay(10_000L)
            val response = PostApi.service.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            for (i in body) {
                i.toShow = false
            }
            //dao.insert(body.toEntity())
            emit(body.size)
        }
    }
        .catch { e -> throw AppError.from(e) }
        .flowOn(Dispatchers.Default)


    override suspend fun save(post: Post) {
        try {
            val response = PostApi.service.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            body.toShow = true
            body.savedOnServer = true
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val file = uploadFile(upload)
            val postWithAttachment = post.copy(
                attachment = Attachment(file.id, AttachmentType.IMAGE),
                toShow = true,
                savedOnServer = true
            )
            save(postWithAttachment)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    private suspend fun uploadFile(upload: MediaUpload): Media {
        try {
            val part = MultipartBody.Part.createFormData(
                "file",
                upload.file.name,
                upload.file.asRequestBody()
            )
            val response = PostApi.service.uploadPic(part)
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
            val response = PostApi.service.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body).apply { toShow = true })

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikeById(id: Long) {
        dao.likeById(id)
        try {
            val response = PostApi.service.dislikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body).apply { toShow = true })

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }


    override suspend fun removeById(id: Long) {
        dao.removeById(id)
        try {
            val response = PostApi.service.removeById(id)
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
            val response = PostApi.service.updateUser(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body()?: throw ApiError(response.code(), response.message())
            val id = body.id
            val token = body.token
            AppAuth.getInstance().setAuth(id,token)

        } catch (e: IOException) {
            throw NetworkError
        }catch (e: Exception) {
            println(e)
            throw UnknownError

        }
    }

    override suspend fun registerUser(login: String, pass: String, name: String) {
        try {
            val response = PostApi.service.registerUser(login, pass, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body()?: throw ApiError(response.code(), response.message())
            val id = body.id
            val token = body.token
            AppAuth.getInstance().setAuth(id,token)

        } catch (e: IOException) {
            throw NetworkError
        }catch (e: Exception) {
            println(e)
            throw UnknownError

        }
    }

}