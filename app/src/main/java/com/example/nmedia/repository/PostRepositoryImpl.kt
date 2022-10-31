package com.example.nmedia.repository


import android.content.Context
import android.widget.Toast
import com.example.nmedia.api.PostApiServiceHolder
import com.example.nmedia.dto.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostRepositoryImpl : PostRepository {
    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        PostApiServiceHolder.service.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()), response.code())
                        return
                    }
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t), 404)
                }
            })
    }


    override fun save(post: Post, callback: PostRepository.Callback<Post>) {
        PostApiServiceHolder.service.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()), response.code())
                        return
                    }
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t), 404)
                }
            })
    }


    override fun likeById(id: Long, callback: PostRepository.Callback<Post>) {
        PostApiServiceHolder.service.likeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()), response.code())
                        return
                    }
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                    print(response.body())
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t), 404)
                }
            })
    }

    override fun dislikeById(id: Long, callback: PostRepository.Callback<Post>) {
        PostApiServiceHolder.service.dislikeById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()), response.code())
                        return
                    }
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t), 404)
                }

            })

    }


    override fun removeById(id: Long, callback: PostRepository.RemCallback) {
        PostApiServiceHolder.service.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()), response.code())
                        return
                    }
                    callback.onSuccess()
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException(t), 404)
                }
            })

    }


}