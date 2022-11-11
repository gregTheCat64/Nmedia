package com.example.nmedia.repository


import androidx.lifecycle.LiveData
import com.example.nmedia.dto.Post

interface PostRepository {
    val data : LiveData<List<Post>>
    suspend fun getAll()
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id: Long)

}