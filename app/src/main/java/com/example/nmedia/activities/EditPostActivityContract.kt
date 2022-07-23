package com.example.nmedia.activities

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.nmedia.dto.ContentData
import com.example.nmedia.dto.Post

class EditPostActivityContract: ActivityResultContract<String, ContentData?>() {
    override fun createIntent(context: Context, input: String): Intent =
         Intent(context, NewPostActivity::class.java)
             .putExtra(Intent.EXTRA_TEXT, input)


    override fun parseResult(resultCode: Int, intent: Intent?): ContentData {
        val videoLink = intent?.getStringExtra("VIDEOLINK")
        val text = intent?.getStringExtra("CONTENT")
        return ContentData(text,videoLink)
    }

}