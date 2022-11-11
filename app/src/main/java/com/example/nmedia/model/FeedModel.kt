package com.example.nmedia.model

import com.example.nmedia.dto.Post

data class FeedModel (
        val posts: List<Post> = emptyList(),
        val empty: Boolean = false,
        )

data class FeedModelState(
        val idle: Boolean = false,
        val loading: Boolean = false,
        val error: Boolean = false,
        val refreshing: Boolean = false,
)