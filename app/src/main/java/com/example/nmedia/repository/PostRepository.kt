package com.example.nmedia.repository

import androidx.lifecycle.LiveData
import com.example.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id:Long)
    fun dislikeById(id:Long)
    fun removeById(id: Long)
    fun save(post: Post)

}