package com.example.nmedia.repository

import androidx.lifecycle.Transformations
import com.example.nmedia.dao.PostDao
import com.example.nmedia.dto.Post
import com.example.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao
):PostRepository {
    override fun getAll() = Transformations.map(dao.getAll()){ list ->
        list.map {
            it.toDto()
        }
    }

    override fun likeById(id: Long) {
        dao.likedById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

//    override fun getPostById(id: Long): Post =
//        dao.getPostById(id).toDto()

}