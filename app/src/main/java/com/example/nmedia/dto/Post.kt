package com.example.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val liked: Boolean,
    val countOfShares: Long,
    val countOfLikes: Long

    )