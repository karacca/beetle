package com.karacca.beetle.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.karacca.beetle.R

/**
 * @author karacca
 * @date 13.07.2022
 */

internal class ReportActivity : AppCompatActivity() {

    private lateinit var screenshot: Uri

    private lateinit var toolbar: MaterialToolbar
    private lateinit var imageView: AppCompatImageView
    private lateinit var screenshotCardView: MaterialCardView
    private lateinit var logsCardView: MaterialCardView

    private val openEditActivity = registerForActivityResult(
        object : ActivityResultContract<Uri, Uri>() {
            override fun createIntent(context: Context, input: Uri?): Intent {
                return Intent(context, EditActivity::class.java).apply {
                    putExtra(ARG_SCREENSHOT, input)
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Uri {
                return intent!!.getParcelableExtra(ARG_SCREENSHOT)!!
            }
        }
    ) {
        screenshot = it
        imageView.setImageURI(null)
        imageView.setImageURI(screenshot)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        screenshot = intent.extras!!.getParcelable(ARG_SCREENSHOT)!!

        toolbar = findViewById(R.id.toolbar)
        imageView = findViewById(R.id.image_view_screenshot)
        screenshotCardView = findViewById(R.id.card_view_screenshot)
        logsCardView = findViewById(R.id.card_view_logs)

        toolbar.setNavigationOnClickListener { finish() }
        imageView.setImageURI(screenshot)
        screenshotCardView.setOnClickListener {
            openEditActivity.launch(screenshot)
        }
    }

    companion object {
        const val ARG_SCREENSHOT = "screenshot"
    }
}
