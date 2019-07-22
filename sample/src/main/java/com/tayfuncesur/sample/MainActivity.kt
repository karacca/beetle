package com.tayfuncesur.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.karacce.beetle.Beetle

class MainActivity : AppCompatActivity() {

    lateinit var beetle: Beetle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Programmatically shake for those who can't shake the emulator
        //beetle =(application as App).beetle
        //beetle.setActivity(this)
        //beetle.onShake()
    }
}
