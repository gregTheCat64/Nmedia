package com.example.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var liked: Boolean,
    var shared: Long,
    var countOfLikes: Long

    )