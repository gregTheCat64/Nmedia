package com.example.nmedia.dto

sealed interface FeedItem {
    val id: Long

}
data class Post(
    override val id: Long,
    val author: String,
    val authorId: Long,
    val authorAvatar: String = "",
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    var attachment: Attachment? = null,
    var savedOnServer: Boolean = false,
    val ownedByMe: Boolean = false
    ): FeedItem

data class Ad(
    override val id: Long,
    val image: String
): FeedItem

data class Attachment(
    val url: String,
    //val description: String?,
    val type: AttachmentType = AttachmentType.IMAGE
)

enum class AttachmentType {
    IMAGE
}