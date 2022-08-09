package com.example.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nmedia.R
import com.example.nmedia.databinding.CardPostBinding
import com.example.nmedia.dto.Post

interface OnInteractionListener{
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onPlay(post: Post)
    fun onOpen(post: Post)
}
//typealias  OnLikeListener = (post: Post) -> Unit
//typealias  OnShareListener = (post: Post) -> Unit
//typealias   OnRemoveListener = (post: Post) -> Unit

class PostAdapter(
    private val listener: OnInteractionListener,

) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding = binding,
            listener = listener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            videoGroup.isVisible = !post.videoLink.isNullOrBlank()
            author.text = post.author + " id: "+ post.id
       //     if (!post.content.isNullOrEmpty()) content.text = post.content else content.text = ""
            content.text = post.content
            published.text = post.published
            like.text = countFormat(post.countOfLikes)
            share.text = countFormat(post.countOfShares)

            like.isChecked = post.likedByMe
            like.setOnClickListener {
                listener.onLike(post)
            }

            share.setOnClickListener {
                listener.onShare(post)
            }

            playBtn.setOnClickListener{
               listener.onPlay(post)
            }
            postGroup.setOnClickListener {
                listener.onOpen(post)
            }

            menu.setOnClickListener { it ->
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)

                    setOnMenuItemClickListener {menuItem ->
                        when (menuItem.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                return@setOnMenuItemClickListener true
                            }
                            R.id.edit -> {
                                listener.onEdit(post)
                                return@setOnMenuItemClickListener true
                            }
                        }

                        false
                    }
                    show()
                }
            }
        }
    }
}

//функция для конвертации 1000 в 1к и тд:
fun countFormat(counter: Long): String {
    val char = counter.toString().toCharArray()
    val firstSymbol = char[0]
    var secondSymbol: Char = '_'
    var thirdSymbol: Char = '_'
    if (counter > 9) secondSymbol = char[1] //чтобы не получить эксепшн, если символов меньше 2х
    if (counter > 99) thirdSymbol = char[2] //чтобы не получить эксепшн, если символов меньше 3х

    return when (counter) {
        in 0..999 -> counter.toString()
        in 1000..9999 -> {
            //если 2я цифра = 0, то просто добавляем К - 1К:
            if (secondSymbol == '0') "${firstSymbol}K" else {
                // в остальных случаях, добавляем десятую - 1.1К:
                "${firstSymbol}.${secondSymbol}K"
            }
        }
        //для десятков тысяч - две цифры и К - 10К:
        in 10_000..99_999 -> "${firstSymbol}${secondSymbol}K"
        //для сотен - три цифры и К:
        in 100_000..999_999 -> "${firstSymbol}${secondSymbol}${thirdSymbol}K"
        //для миллиона по аналогии:
        in 1000_000..9999_999 -> {
            if (secondSymbol == '0') "${firstSymbol}M"
            else {
                "${firstSymbol}.${secondSymbol}M"
            }
        }
        in 10_000_000..99_999_999 -> "${firstSymbol}${secondSymbol}M"
        in 100_000_000..999_999_999 -> "${firstSymbol}${secondSymbol}${thirdSymbol}M"
        else -> "infinity"
    }
}


