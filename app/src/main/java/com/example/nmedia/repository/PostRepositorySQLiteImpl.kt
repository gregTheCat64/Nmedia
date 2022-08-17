package com.example.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.nmedia.dao.PostDao
import com.example.nmedia.dto.Post
import com.example.nmedia.entity.PostEntity

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()


    override fun getAll(): LiveData<List<Post>> = dao.getAll().map {
        it.map (
            PostEntity::toDto
        )
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun likeById(id: Long) {
        dao.likedById(id)
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Long) {
        dao.removeById(id)

    }
}