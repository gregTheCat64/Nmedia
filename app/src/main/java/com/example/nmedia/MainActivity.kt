package com.example.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.nmedia.adapter.PostAdapter
import com.example.nmedia.adapter.PostEventListener
import com.example.nmedia.databinding.ActivityMainBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.util.AndroidUtils
import com.example.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(
            object : PostEventListener {
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.sharedById(post.id)
                }
            }
        )

        viewModel.edited.observe(this) { edited ->
            if (edited.id == 0L) {
                    return@observe
            }
            if (!edited.content.isNullOrBlank()){
                binding.editCancelGroup.visibility = View.VISIBLE
            } else binding.editCancelGroup.visibility = View.GONE

            binding.content.setText(edited.content)
            binding.content.requestFocus()

        }

        //    binding.content.setOnFocusChangeListener { v, hasFocus -> binding.editCancelGroup.visibility = View.VISIBLE }


        binding.save.setOnClickListener {
            if (binding.content.text.isNullOrBlank()) {
                Toast.makeText(it.context, "Post is blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val text = binding.content.text.toString()


            viewModel.editContent(text)
            viewModel.save()

            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
            binding.content.setText("")
        }
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)

        }
    }

}

