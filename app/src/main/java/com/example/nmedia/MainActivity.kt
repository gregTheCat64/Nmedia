package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. " +
                    "Затем появились курсы по дизайну, разработке, аналитике и управлению. " +
                    "Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. " +
                    "Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                    "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            liked = false,
            shared = 1099,
            countOfLikes = 109999999

        )

        //setText пишу по привычке. На мой взгляд, так код выглядит нагляднее :

        with(binding) {
            avatar.setImageResource(R.drawable.post_avatar)
            authorTextView.setText(post.author)
            contentTextView.setText(post.content)
            publishedTextView.setText(post.published)
            likesTextView.setText(countFormat(post.countOfLikes))
            sharesTextView.setText(countFormat(post.shared))
            if (post.liked) {
                favImage.setImageResource(R.drawable.ic_liked_24)
            }
            favImage.setOnClickListener {
                if (!post.liked) {
                    favImage.setImageResource(R.drawable.ic_liked_24)
                    post.countOfLikes++
                } else {
                    favImage.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    post.countOfLikes--
                }
                likesTextView.setText(countFormat(post.countOfLikes))
                post.liked = !post.liked
            }
            sharingImage.setOnClickListener {
                post.shared++
                sharesTextView.setText(countFormat(post.shared))
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
            else {"${firstSymbol}.${secondSymbol}M"
            }
        }
        in 10_000_000..99_999_999 -> "${firstSymbol}${secondSymbol}M"
        in 100_000_000..999_999_999 -> "${firstSymbol}${secondSymbol}${thirdSymbol}M"
        else -> "infinity"
    }
}