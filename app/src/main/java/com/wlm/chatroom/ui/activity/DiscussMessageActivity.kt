package com.wlm.chatroom.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wlm.chatroom.R

class DiscussMessageActivity : AppCompatActivity() {

    companion object {
        const val KEY_DISCUSS_ID = "KEY_DISCUSS_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discuss_message)
    }
}
