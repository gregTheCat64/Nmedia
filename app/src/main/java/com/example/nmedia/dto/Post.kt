package com.example.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String?,
    val published: String,
    val likedByMe: Boolean,
    val countOfShares: Long,
    val countOfLikes: Long,
    val videoLink: String?

    )