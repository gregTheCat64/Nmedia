package com.example.nmedia.repository

import com.example.nmedia.dto.MediaUpload
import com.example.nmedia.dto.Post
import com.example.nmedia.entity.PostEntity
import com.example.nmedia.model.PhotoModel
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val data : Flow<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id: Long)
    suspend fun updateShownStatus()
    suspend fun updateUser(login: String, pass: String)
    suspend fun registerUser(login: String, pass: String, name: String)

}