package com.karacca.beetle.ui

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.appbar.MaterialToolbar
import com.karacca.beetle.R

/**
 * @author karacca
 * @date 13.07.2022
 */

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        findViewById<MaterialToolbar>(R.id.toolbar).apply {
            setNavigationOnClickListener { finish() }
        }

        val screenshot = intent.extras?.getParcelable(ARG_SCREENSHOT) as? Uri
        if (screenshot != null) {
            findViewById<AppCompatImageView>(R.id.image_view_screenshot).apply {
                setImageURI(screenshot)
            }
        }
    }

    companion object {
        const val ARG_SCREENSHOT = "screenshot"
    }
}
