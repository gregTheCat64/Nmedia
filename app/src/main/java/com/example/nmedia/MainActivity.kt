package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this) { post ->
            with(binding) {
                avatar.setImageResource(R.drawable.post_avatar)
                authorTextView.setText(post.author)
                contentTextView.setText(post.content)
                publishedTextView.setText(post.published)

                val likeImage = if (post.liked) {
                    R.drawable.ic_liked_24
                } else {
                    R.drawable.ic_baseline_favorite_border_24
                }
                favImage?.setImageResource(likeImage)

                likesTextView?.setText(countFormat(post.countOfLikes))
                sharesTextView?.setText(countFormat(post.countOfShares))
            }
        }
        binding.favImage?.setOnClickListener {
            viewModel.like()
        }
        binding.sharingImage.setOnClickListener {
            viewModel.share()
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