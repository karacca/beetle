/*
 * Copyright 2022 Omer Karaca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karacca.beetle.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.button.MaterialButton
import com.karacca.beetle.R
import com.karacca.beetle.ui.widget.FingerPaintImageView
import com.karacca.beetle.utils.CollectDataTask

/**
 * @author karacca
 * @date 18.07.2022
 */

@Suppress("DEPRECATION")
internal class EditScreenshotActivity : AppCompatActivity(), CollectDataTask.OnCollectDataTaskListener {

    private lateinit var screenshot: Uri

    private lateinit var screenshotImageView: FingerPaintImageView
    private lateinit var closeButton: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var undoButton: AppCompatImageButton
    private lateinit var yellowView: View
    private lateinit var blackView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        screenshot = intent.extras!!.getParcelable(FeedbackActivity.ARG_SCREENSHOT)!!

        screenshotImageView = findViewById(R.id.image_view_screenshot)
        closeButton = findViewById(R.id.button_close)
        saveButton = findViewById(R.id.button_save)
        undoButton = findViewById(R.id.button_undo)
        yellowView = findViewById<View>(R.id.view_yellow).apply { isSelected = true }
        blackView = findViewById(R.id.view_black)

        screenshotImageView.setImageURI(screenshot)

        closeButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra(FeedbackActivity.ARG_SCREENSHOT, screenshot)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        saveButton.setOnClickListener {
            val task = CollectDataTask(this@EditScreenshotActivity, this@EditScreenshotActivity)
            task.execute(screenshotImageView.drawable?.toBitmap())
        }

        undoButton.setOnClickListener {
            screenshotImageView.undo()
        }

        yellowView.setOnClickListener {
            yellowView.isSelected = !yellowView.isSelected
            blackView.isSelected = !yellowView.isSelected
            screenshotImageView.strokeColor = ContextCompat.getColor(this, R.color.yellow_50)
        }

        blackView.setOnClickListener {
            blackView.isSelected = !blackView.isSelected
            yellowView.isSelected = !blackView.isSelected
            screenshotImageView.strokeColor = ContextCompat.getColor(this, android.R.color.black)
        }
    }

    override fun onDataReady(data: Uri?) {
        val intent = Intent()
        intent.putExtra(FeedbackActivity.ARG_SCREENSHOT, data)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
