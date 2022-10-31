package com.example.nmedia.repository


import android.content.Context
import com.example.nmedia.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun save(post: Post, callback: Callback<Post>)
    fun removeById(id: Long, callback: RemCallback)
    fun likeById(id: Long, callback: Callback<Post>)
    fun dislikeById(id: Long,callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception, code:Int) {}
    }

    interface RemCallback{
        fun onSuccess()
        fun onError(e:Exception, code: Int){}
    }
}