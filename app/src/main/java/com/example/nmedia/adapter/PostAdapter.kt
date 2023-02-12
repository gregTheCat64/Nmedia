package com.example.nmedia.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nmedia.R
import com.example.nmedia.databinding.CardAdBinding
import com.example.nmedia.databinding.CardPostBinding
import com.example.nmedia.dto.Ad
import com.example.nmedia.dto.FeedItem
import com.example.nmedia.dto.Post
import com.example.nmedia.view.load

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
    fun onImage(post: Post) {}
}

const val BASE_URL = "http://10.0.2.2:9999"

class PostAdapter(
    private val onInteractionListener: OnInteractionListener,
) : PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(PostDiffCallback()) {
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)){
            is Ad -> R.layout.card_ad
            is Post -> R.layout.card_post
            null -> error("unknown item type")
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            R.layout.card_post -> {
                val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                 PostViewHolder(binding, onInteractionListener)
            }
            R.layout.card_ad -> {
                val binding = CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                 AdViewHolder(binding)
            }
            else -> error("unknown item type: $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val post = getItem(position) ?: return
//        holder.bind(post)
        when (val item = getItem(position)){
            is Ad -> (holder as? AdViewHolder)?.bind(item)
            is Post -> (holder as? PostViewHolder)?.bind(item)
            null -> error("unknown item type")
        }
    }
}

class AdViewHolder(
    private val binding: CardAdBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad) {
        binding.image.load("$BASE_URL/media/${ad.image}")
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(post: Post) {
        getAvatars(post,binding)
        if (post.attachment!=null){
            binding.attachImage.visibility = View.VISIBLE
            getAttachment(post,binding)
        } else binding.attachImage.visibility = View.GONE
        if (!post.savedOnServer){
            binding.like.visibility = View.INVISIBLE
            binding.share.visibility = View.INVISIBLE
        } else {
            binding.like.visibility = View.VISIBLE
            binding.share.visibility = View.VISIBLE
        }
        if (post.savedOnServer){
            binding.savedOnServer.setImageResource(R.drawable.ic_baseline_public_24)
        } else binding.savedOnServer.setImageResource(R.drawable.ic_baseline_public_off_24)



        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe
            like.text = "${post.likes}"
            menu.isVisible = post.ownedByMe

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            attachImage.setOnClickListener {
                onInteractionListener.onImage(post)
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.25F, 1F)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.25F, 1F)
                ObjectAnimator.ofPropertyValuesHolder(it, scaleX, scaleY).apply {
                    duration = 500
                    repeatCount = 100
                    interpolator = BounceInterpolator()
                }.start()
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }
}

fun getAvatars(post: Post, binding: CardPostBinding){
    Glide.with(binding.avatar)
        .load("$BASE_URL/avatars/${post.authorAvatar}")
        .placeholder(R.drawable.ic_baseline_man_24)
        .error(R.drawable.ic_baseline_cancel_24)
        .circleCrop()
        .timeout(10_000)
        .into(binding.avatar)
}

fun getAttachment(post: Post, binding: CardPostBinding){
    Glide.with(binding.attachImage)
        .load("$BASE_URL/media/${post.attachment?.url}")
        .error(R.drawable.ic_baseline_cancel_24)
        .timeout(10_000)
        .into(binding.attachImage)
}

//data class Payload(
//    val likedByMe: Boolean? = null,
//    val content: String? = null
//)

class PostDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class){
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }

//    override fun getChangePayload(oldItem: FeedItem, newItem: FeedItem): Any =
//        Payload(
//            likedByMe = newItem.id
//        )
}

