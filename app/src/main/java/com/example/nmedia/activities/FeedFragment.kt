package com.example.nmedia.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.nmedia.R
import com.example.nmedia.adapter.BASE_URL
import com.example.nmedia.adapter.OnInteractionListener
import com.example.nmedia.adapter.PostAdapter
import com.example.nmedia.databinding.CardPostBinding
import com.example.nmedia.databinding.FragmentFeedBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.viewmodel.PostViewModel
import com.google.android.material.snackbar.Snackbar

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                if (post.likedByMe){
                    viewModel.dislikeById(post.id)
                } else {viewModel.likeById(post.id)}
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            val newPost = state.posts.size>adapter.currentList.size
            adapter.submitList(state.posts){
                if (newPost) binding.list.smoothScrollToPosition(0)
            }
            binding.emptyText.isVisible = state.empty

        }
        viewModel.dataState.observe(viewLifecycleOwner){ state ->
            binding.progress.isVisible = state.loading
            if (state.error){
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG).setAction(R.string.retry_loading){
                    viewModel.refresh()
                }
                    .show()
            }
            //binding.errorGroup.isVisible = state.error
            binding.swiprefresh.isRefreshing = state.refreshing
        }


        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swiprefresh.setOnRefreshListener {
            viewModel.refresh()

        }

        return binding.root
    }

}


