package com.example.nmedia.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nmedia.R
import com.example.nmedia.databinding.ActivityNewPostBinding
import com.example.nmedia.util.AndroidUtils

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Toast.makeText(this, "Новый пост", Toast.LENGTH_SHORT).show()
            } else binding.content.setText(text.toString())
        }


        binding.save.setOnClickListener { it ->
            var result: Intent = Intent()
            result.run {
                if (binding.videoLink.text.isNullOrBlank() && binding.content.text.isNullOrBlank()) {
                    Toast.makeText(it.context, getString(R.string.PostIsBlank), Toast.LENGTH_SHORT)
                        .show()
                    setResult(RESULT_CANCELED)
                    finish()
                }
                if (binding.videoLink.text.isNotBlank()) {
                    this.putExtra("VIDEOLINK", binding.videoLink.text.toString())
                }
                if (binding.content.text.isNotBlank()) {
                    this.putExtra("CONTENT", binding.content.text.toString())
                }
                setResult(RESULT_OK, result)
                finish()
            }


        }


    }

}

