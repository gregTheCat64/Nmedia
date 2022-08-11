package com.example.nmedia.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatViewInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nmedia.R
import com.example.nmedia.activities.NewPostFragment.Companion.textArg
import com.example.nmedia.adapter.OnInteractionListener
import com.example.nmedia.adapter.PostAdapter

import com.example.nmedia.databinding.FragmentFeedBinding
import com.example.nmedia.dto.Post
import com.example.nmedia.viewmodel.PostViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class FeedFragment : Fragment() {
    val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )



        val adapter = PostAdapter(
            object : OnInteractionListener {

                override fun onEdit(post: Post) {
                    val action = FeedFragmentDirections.actionFeedFragmentToNewPostFragment(post.content.toString(),post.videoLink.toString())
                    findNavController().navigate(action)
                    viewModel.edit(post)
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

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoLink))
//                    if (intent.resolveActivity(packageManager) != null){ // !!здесь не работает packageManager, пришлось избавиться от проверки
                        startActivity(intent)
              //      }
                }

                override fun onPost(post: Post) {
                    val action = FeedFragmentDirections.actionFeedFragmentToCurrentPostFragment(post.id)
                    findNavController().navigate(action)
                }
            }
        )

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = posts.size>adapter.currentList.size
            adapter.submitList(posts){
                if(newPost) binding.list.smoothScrollToPosition(0)
            }
        }


        binding.create.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToNewPostFragment("","")
            findNavController().navigate(action)
        }



        return binding.root
    }

}

