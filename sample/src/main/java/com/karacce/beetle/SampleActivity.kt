package com.karacce.beetle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_sample.*

/**
 * @user: omerkaraca
 * @date: 2019-07-06
 */

class SampleActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        root.setOnClickListener {
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Beetle.ACTION_SHAKE))
        }
    }
}