package com.example.nmedia.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentNewPostBinding
import com.example.nmedia.util.AndroidUtils
import com.example.nmedia.util.StringArg
import com.example.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object{
        var Bundle.textArg: String? by StringArg

    }

    val args by navArgs<NewPostFragmentArgs>()

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )


        binding.content.setText(args.content)
        if (!args.videoLink.isNullOrEmpty()){
            binding.videoLink.visibility = View.VISIBLE
            binding.videoLink.setText(args.videoLink.toString())
        }
        binding.attachBtn.setOnClickListener {
            binding.videoLink.visibility = View.VISIBLE
        }


        binding.content.requestFocus()

        binding.save.setOnClickListener {
        viewModel.changeContent(binding.content.text.toString(), binding.videoLink.text.toString())

            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())


            findNavController().navigateUp()

        }
        return binding.root
    }

    }



