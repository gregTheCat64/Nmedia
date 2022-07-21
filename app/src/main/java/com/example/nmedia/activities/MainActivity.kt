package com.example.nmedia.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.nmedia.R
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

       val newPostLauncher =  registerForActivityResult(NewPostActivityContract()){ result->
            result?: return@registerForActivityResult
      //     Toast.makeText(this, "${result.textContent} and ${result.videoContent}", Toast.LENGTH_SHORT).show()
            viewModel.editContent(result)
            viewModel.save()
        }
        val editPostLauncher = registerForActivityResult(EditPostActivityContract()){result->
            result?: return@registerForActivityResult
            viewModel.editContent(result)
            viewModel.save()
        }


        val adapter = PostAdapter(
            object : PostEventListener {
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    editPostLauncher.launch(post.content)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                    binding.list.smoothScrollToPosition(0)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.sharedById(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                }

                @SuppressLint("QueryPermissionsNeeded")
                override fun onPlay(post: Post) {
                   Toast.makeText(applicationContext, post.videoLink.toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoLink))
                    if (intent.resolveActivity(packageManager) != null){
                        startActivity(intent)
                    }
                }
            }
        )

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size>adapter.currentList.size
            adapter.submitList(posts){
                if(newPost) binding.list.smoothScrollToPosition(0)
            }
        }


        binding.create.setOnClickListener {
            newPostLauncher.launch()
        }
    }

}

