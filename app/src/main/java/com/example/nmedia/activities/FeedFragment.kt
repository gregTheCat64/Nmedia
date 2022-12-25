package com.example.nmedia.activities


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.nmedia.R
import com.example.nmedia.adapter.OnInteractionListener
import com.example.nmedia.adapter.PostAdapter
import com.example.nmedia.auth.AppAuth
import com.example.nmedia.databinding.FragmentFeedBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()

    @Inject
    lateinit var appAuth: AppAuth

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
                if (appAuth.authStateFlow.value.id != 0L) {
                    if (post.likedByMe) {
                        viewModel.dislikeById(post.id)
                    } else {
                        viewModel.likeById(post.id)
                    }
                } else
                    //showSignInDialog()
                    println("noSign")

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

            override fun onImage(post: Post) {
                val action =
                    FeedFragmentDirections.actionFeedFragmentToImageFragment(post.attachment?.url.toString())
                findNavController().navigate(action)
            }
        })

        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swiprefresh.isRefreshing =
                it.refresh is LoadState.Loading
                        || it.append is LoadState.Loading
                        || it.prepend is LoadState.Loading
            }
        }
//        viewModel.data.observe(viewLifecycleOwner) { state ->
//            val newPost = state.posts.size > adapter.currentList.size
//            adapter.submitList(state.posts) {
//                if (newPost) binding.list.smoothScrollToPosition(0)
//            }
//            binding.emptyText.isVisible = state.empty
//
//        }

//        viewModel.dataState.observe(viewLifecycleOwner) { state ->
//            binding.progress.isVisible = state.loading
//            binding.swiprefresh.isRefreshing = state.refreshing
//            if (state.error) {
//                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
//                    .setAction(R.string.retry_loading) {
//                        viewModel.refresh()
//                    }
//                    .show()
//            }
//            //binding.errorGroup.isVisible = state.error
//            binding.swiprefresh.isRefreshing = state.refreshing
//        }

//        viewModel.newerCount.observe(viewLifecycleOwner) { state ->
//
//            if (state != 0) {
//                val btnText = "Новая запись ($state)"
//                binding.newerPostsBtn.text = btnText
//                binding.newerPostsBtn.visibility = View.VISIBLE
//            }
//
//            println("state: $state")
//        }

//        binding.newerPostsBtn.setOnClickListener {
//            viewModel.loadPosts()
//            viewModel.updateShownStatus()
//            it.visibility = View.GONE
//        }
//
//
//        binding.retryButton.setOnClickListener {
//            viewModel.loadPosts()
//        }
//
//        binding.createBtn.setOnClickListener {
//            if (appAuth.authStateFlow.value.token != null) {
//                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
//            } else showSignInDialog()
//        }
//
//        binding.swiprefresh.setOnRefreshListener {
//            viewModel.refresh()
//        }
//
//
        return binding.root
    }
//
//    private fun showSignInDialog() {
//        val listener = DialogInterface.OnClickListener { _, which ->
//            when (which) {
//                DialogInterface.BUTTON_POSITIVE -> findNavController().navigate(R.id.action_feedFragment_to_signInFragment)
//                DialogInterface.BUTTON_NEGATIVE -> Toast.makeText(
//                    context,
//                    "Не забудьте авторизоваться",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//        val dialog = AlertDialog.Builder(context)
//            .setCancelable(false)
//            .setTitle("Вы не авторизованы!")
//            .setMessage("Пожалуйста, авторизуйтесь")
//            .setPositiveButton("Хорошо", listener)
//            .setNegativeButton("Позже", listener)
//            .create()
//
//        dialog.show()
//    }

}


