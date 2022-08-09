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
import com.example.nmedia.R
import com.example.nmedia.databinding.FragmentNewPostBinding
import com.example.nmedia.util.AndroidUtils
import com.example.nmedia.util.StringArg
import com.example.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object{
        var Bundle.textArg: String? by StringArg
    }
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

        arguments?.textArg
            ?.let (binding.content::setText)
//
//        val intent = Intent()
//        intent?.let {
//            val text = it.getStringExtra(Intent.EXTRA_TEXT)
//            if (text.isNullOrBlank()) {
//            //    Toast.makeText(this, "Новый пост", Toast.LENGTH_SHORT).show()
//            } else binding.content.setText(text.toString())
//        }

        binding.content.requestFocus()
        binding.save.setOnClickListener {
        viewModel.changeContent(binding.content.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())

//            var result: Intent = Intent()
//            result.run {
//                if (binding.videoLink.text.isNullOrBlank() && binding.content.text.isNullOrBlank()) {
//                    Toast.makeText(it.context, getString(R.string.PostIsBlank), Toast.LENGTH_SHORT)
//                        .show()
//                    activity?.setResult(Activity.RESULT_CANCELED)
//
//                }
//                if (binding.videoLink.text.isNotBlank()) {
//                    this.putExtra("VIDEOLINK", binding.videoLink.text.toString())
//                }
//                if (binding.content.text.isNotBlank()) {
//                    this.putExtra("CONTENT", binding.content.text.toString())
//                }
//                activity?.setResult(Activity.RESULT_OK, result)
//
//            }
            findNavController().navigateUp()


        }
        return binding.root
    }

    }



