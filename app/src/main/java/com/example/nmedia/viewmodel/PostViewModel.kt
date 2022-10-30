package com.example.nmedia.viewmodel

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nmedia.databinding.FragmentFeedBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.model.FeedModel
import com.example.nmedia.repository.*
import com.example.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = "",
    attachment = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
                println("success")
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
                println("error")
            }
        }, getApplication())
    }

    fun save() {
        var oldPosts = _data.value?.posts.orEmpty()
        edited.value?.let {
            repository.save(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(post: Post) {
                    oldPosts = listOf(post)+oldPosts
                    _data.postValue(FeedModel(posts = oldPosts))
                    _postCreated.value = Unit
                }

                override fun onError(e: Exception) {
                    _data.value = FeedModel(error = true)
                }

            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        var oldPosts = _data.value?.posts.orEmpty()
        repository.likeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(post: Post) {
                oldPosts = oldPosts.map {
                    if (it.id != id) it else it.copy(
                        likedByMe = post.likedByMe,
                        likes = post.likes
                    )
                }
                _data.value = FeedModel(posts = oldPosts)
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }

        })
    }


    fun dislikeById(id: Long) {
        var oldPosts = _data.value?.posts.orEmpty()
        repository.dislikeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(post: Post) {
                oldPosts = oldPosts.map {
                    if (it.id != id) it else it.copy(
                        likedByMe = post.likedByMe,
                        likes = post.likes
                    )
                }
                _data.value = FeedModel(posts = oldPosts)
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }

        })
    }

    fun refresh() {
        loadPosts()
    }

    fun removeById(id: Long) {

        repository.removeById(id, object : PostRepository.RemCallback {
            override fun onSuccess() {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }
        })

    }

}