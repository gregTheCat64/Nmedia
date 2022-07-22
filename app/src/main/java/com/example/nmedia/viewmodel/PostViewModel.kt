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
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(contentData: ContentData) {
        val text = contentData.textContent?.trim()
        if (edited.value?.content == text) {
            return
        }
        if (!contentData.textContent.isNullOrBlank() && !contentData.videoContent.isNullOrBlank()){
            edited.value = edited.value?.copy(
                content = contentData.textContent,
                videoLink = contentData.videoContent
            )
        } else if (!contentData.textContent.isNullOrBlank()){
            edited.value = edited.value?.copy(
                content = contentData.textContent
            )
        } else if(!contentData.videoContent.isNullOrBlank()){
            edited.value = edited.value?.copy(
                content = contentData.videoContent
            )
        }

    }

}

//fun cancelEditing() {
//    edited.value = empty
//}
