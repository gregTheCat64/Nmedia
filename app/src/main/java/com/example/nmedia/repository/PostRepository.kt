package com.example.nmedia.repository

import androidx.paging.PagingData
import com.example.nmedia.dto.Media
import com.example.nmedia.dto.MediaUpload
import com.example.nmedia.dto.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    val data : Flow<PagingData<Post>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(post: Post, upload: MediaUpload?)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id: Long)
    //suspend fun updateShownStatus()
    suspend fun updateUser(login: String, pass: String)
    suspend fun registerUser(login: String, pass: String, name: String)

}