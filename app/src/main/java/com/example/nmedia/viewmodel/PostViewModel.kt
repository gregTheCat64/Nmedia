package com.example.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nmedia.dto.ContentData
import com.example.nmedia.dto.Post
import com.example.nmedia.repository.InMemoryPostRepository
import com.example.nmedia.repository.PostRepository

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

class PostViewModel : ViewModel() {
    private val repository: PostRepository = InMemoryPostRepository()
    val data = repository.getAll()
    fun likeById(id: Long) = repository.likeById(id)
    fun sharedById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    private val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun editContent(contentData: ContentData) {
        edited.value?.let {
            val trimmed = contentData.textContent?.trim()
            if (trimmed == it.content) {
                return
            }
 //           edited.value = it.copy(content = contentData.textContent, videoLink = contentData.videoContent)
            if (!contentData.videoContent.isNullOrBlank() && !contentData.textContent.isNullOrBlank()) {
                edited.value = it.copy(content = trimmed, videoLink = contentData.videoContent)
            } else if (!contentData.textContent.isNullOrBlank()) edited.value = it.copy(content = trimmed)
            else  edited.value = it.copy(videoLink = contentData.videoContent)
        }

    }

    fun cancelEditing() {
        edited.value = empty
    }
}