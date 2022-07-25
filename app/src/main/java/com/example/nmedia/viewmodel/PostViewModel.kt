package com.example.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nmedia.dto.ContentData
import com.example.nmedia.dto.Post
import com.example.nmedia.repository.InMemoryPostRepository
import com.example.nmedia.repository.PostRepository
import com.example.nmedia.repository.PostRepositoryFileImpl
import com.example.nmedia.repository.PostRepositorySharedPrefsImpl

val empty = Post(
    0,
    "",
    "",
    "",
    false,
    0,
    0,
    null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    fun likeById(id: Long) = repository.likeById(id)
    fun sharedById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    private val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(contentData: ContentData) {
        val text = contentData.textContent?.trim()
        val videoLink = contentData.videoContent?.trim()
        if (edited.value?.content == text && edited.value?.videoLink ==videoLink) {
            return
        }
        edited.value = edited.value?.copy(
                content = contentData.textContent,
                videoLink = contentData.videoContent)
    }

}

//fun cancelEditing() {
//    edited.value = empty
//}
