package com.example.nmedia.activities

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.nmedia.R
import com.example.nmedia.adapter.BASE_URL
import com.example.nmedia.databinding.FragmentImageBinding
import com.example.nmedia.viewmodel.PostViewModel

class ImageFragment: Fragment() {

    private val args by navArgs<ImageFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageBinding.inflate(
            inflater,
            container,
            false
        )
        val url = args.imageArg.toUri()

        Glide.with(binding.imageFull)
            .load("$BASE_URL/media/$url")
            .error(R.drawable.ic_baseline_cancel_24)
            .timeout(10_000)
            .into(binding.imageFull)

    return binding.root
    }
}