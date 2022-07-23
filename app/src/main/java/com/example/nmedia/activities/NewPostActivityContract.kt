package com.example.nmedia.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import com.example.nmedia.dto.ContentData

class NewPostActivityContract: ActivityResultContract<Unit, ContentData?>() {
    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(context, NewPostActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): ContentData {

        val videoLink = intent?.getStringExtra("VIDEOLINK")
        val text = intent?.getStringExtra("CONTENT")
        return ContentData(text, videoLink)
    }
}
