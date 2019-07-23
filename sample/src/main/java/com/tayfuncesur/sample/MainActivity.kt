package com.tayfuncesur.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.karacce.beetle.Beetle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shake.setOnClickListener {
            LocalBroadcastManager.getInstance(this)
                .sendBroadcast(Intent(Beetle.ACTION_SHAKE))
        }
    }
}
