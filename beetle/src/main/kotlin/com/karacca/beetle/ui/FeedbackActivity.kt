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

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.karacca.beetle.BeetleConfig
import com.karacca.beetle.R
import com.karacca.beetle.data.model.Collaborator
import com.karacca.beetle.data.model.Image
import com.karacca.beetle.data.model.Label
import com.karacca.beetle.data.repository.GitHubRepository
import com.karacca.beetle.data.repository.ImageRepository
import com.karacca.beetle.ui.adapter.AssigneeAdapter
import com.karacca.beetle.ui.adapter.LabelAdapter
import com.karacca.beetle.ui.widget.HorizontalItemDecorator
import com.karacca.beetle.utils.DeviceUtils
import com.karacca.beetle.utils.MarkdownUtils
import com.karacca.beetle.utils.NotificationUtils
import kotlinx.coroutines.launch
import org.bouncycastle.util.io.pem.PemReader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

/**
 * @author karacca
 * @date 13.07.2022
 */

internal class FeedbackActivity : AppCompatActivity(), TextWatcher {

    private lateinit var screenshot: Uri
    private lateinit var config: BeetleConfig

    private lateinit var gitHubRepository: GitHubRepository
    private lateinit var imageRepository: ImageRepository

    private lateinit var toolbar: MaterialToolbar
    private lateinit var titleEditText: TextInputEditText
    private lateinit var assigneesTextView: MaterialTextView
    private lateinit var assigneesRecyclerView: RecyclerView
    private lateinit var assigneesDividerView: View
    private lateinit var labelsTextView: MaterialTextView
    private lateinit var labelsRecyclerView: RecyclerView
    private lateinit var labelsDividerView: View
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var screenshotCardView: MaterialCardView
    private lateinit var screenshotCheckBox: MaterialCheckBox
    private lateinit var screenshotImageView: AppCompatImageView
    private lateinit var logsCardView: MaterialCardView
    private lateinit var logsCheckBox: MaterialCheckBox
    private lateinit var loadingLayout: ConstraintLayout

    private lateinit var assigneesAdapter: AssigneeAdapter
    private lateinit var labelAdapter: LabelAdapter

    private val openEditScreenshotActivity = registerForActivityResult(
        object : ActivityResultContract<Uri, Uri?>() {
            override fun createIntent(context: Context, input: Uri?): Intent {
                return Intent(context, EditScreenshotActivity::class.java).apply {
                    putExtra(ARG_SCREENSHOT, input)
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return intent?.getParcelableExtra(ARG_SCREENSHOT)
            }
        }
    ) {
        if (it != null) {
            screenshot = it
            screenshotImageView.setImageURI(null)
            screenshotImageView.setImageURI(screenshot)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        screenshot = intent.extras!!.getParcelable(ARG_SCREENSHOT)!!
        config = Gson().fromJson(intent.extras!!.getString(ARG_CONFIG)!!, BeetleConfig::class.java)

        imageRepository = ImageRepository()
        gitHubRepository = GitHubRepository(
            getPrivateKey(application),
            config.organization!!,
            config.repository!!
        )

        toolbar = findViewById(R.id.toolbar)
        titleEditText = findViewById(R.id.edit_text_title)
        assigneesTextView = findViewById(R.id.text_view_assignees)
        assigneesRecyclerView = findViewById(R.id.recycler_view_assignees)
        assigneesDividerView = findViewById(R.id.view_assignees_divider)
        labelsTextView = findViewById(R.id.text_view_labels)
        labelsRecyclerView = findViewById(R.id.recycler_view_labels)
        labelsDividerView = findViewById(R.id.view_labels_divider)
        descriptionEditText = findViewById(R.id.edit_text_description)
        screenshotCardView = findViewById(R.id.card_view_screenshot)
        screenshotCheckBox = findViewById(R.id.checkbox_screenshot)
        screenshotImageView = findViewById(R.id.image_view_screenshot)
        logsCardView = findViewById(R.id.card_view_logs)
        logsCheckBox = findViewById(R.id.checkbox_logs)
        loadingLayout = findViewById(R.id.layout_loading)

        toolbar.setNavigationOnClickListener { finish() }
        toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener if (it.itemId == R.id.send) {
                createIssue()
                true
            } else {
                false
            }
        }

        screenshotImageView.setImageURI(screenshot)
        screenshotCardView.setOnClickListener {
            openEditScreenshotActivity.launch(screenshot)
        }

        logsCardView.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.feedback_logs_title)
                .setItems(
                    (DeviceUtils.getDeviceData(this) + config.extras).map {
                        "${it.key}: ${it.value}"
                    }.toTypedArray()
                ) { _, _ -> }
                .show()
        }

        titleEditText.addTextChangedListener(this)
        descriptionEditText.addTextChangedListener(this)

        execute {
            if (config.enableAssignees) {
                setAssignees(gitHubRepository.getCollaborators())
            }

            if (config.enableLabels) {
                setLabels(gitHubRepository.getLabels())
            }
        }

        if (config.enableAssignees) {
            assigneesAdapter = AssigneeAdapter {
                val assignees = assigneesAdapter.currentList
                assignees[it].selected = !assignees[it].selected
                assigneesAdapter.submitList(assignees)
            }

            assigneesRecyclerView.apply {
                addItemDecoration(HorizontalItemDecorator(8))
                adapter = assigneesAdapter
                layoutManager = LinearLayoutManager(
                    this@FeedbackActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }

        if (config.enableLabels) {
            labelAdapter = LabelAdapter {
                val labels = labelAdapter.currentList
                labels[it].selected = !labels[it].selected
                labelAdapter.submitList(labels)
            }

            labelsRecyclerView.apply {
                addItemDecoration(HorizontalItemDecorator(8))
                adapter = labelAdapter
                layoutManager = LinearLayoutManager(
                    this@FeedbackActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun afterTextChanged(p0: Editable?) {
        val isTitleMissing = titleEditText.text.isNullOrEmpty()
        val isDescriptionMissing = descriptionEditText.text.isNullOrEmpty()
        toolbar.menu[0].isEnabled = !isTitleMissing && !isDescriptionMissing
    }

    private fun getPrivateKey(context: Context): PrivateKey {
        val file = context.assets.open("beetle.pem")
        val inputStreamReader = InputStreamReader(file)
        val readerBufferedFile = BufferedReader(inputStreamReader)
        val reader = PemReader(readerBufferedFile)
        val privateKeyPem = reader.readPemObject()

        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyPem.content))
    }

    private fun setAssignees(assignees: List<Collaborator>) {
        assigneesAdapter.submitList(assignees)
        assigneesTextView.isVisible = true
        assigneesRecyclerView.isVisible = true
        assigneesDividerView.isVisible = true
    }

    private fun setLabels(labels: List<Label>) {
        labelAdapter.submitList(labels)
        labelsTextView.isVisible = true
        labelsRecyclerView.isVisible = true
        labelsDividerView.isVisible = true
    }

    private fun createIssue() {
        val title = titleEditText.text?.toString() ?: ""
        val description = descriptionEditText.text?.toString() ?: ""
        val assignees = if (config.enableAssignees) {
            assigneesAdapter.currentList.filter { it.selected }.map { it.login }
        } else {
            arrayListOf()
        }

        val labels = if (config.enableLabels) {
            labelAdapter.currentList.filter { it.selected }.map { it.name }
        } else {
            arrayListOf()
        }

        execute {
            val image: Image? = if (screenshotCheckBox.isChecked) {
                imageRepository.uploadImage(screenshot)
            } else {
                null
            }

            val descriptionMarkdown = MarkdownUtils.createDescription(
                this,
                description,
                image?.image?.url,
                if (logsCheckBox.isChecked) {
                    DeviceUtils.getDeviceData(this)
                } else {
                    hashMapOf()
                },
                if (logsCheckBox.isChecked) {
                    config.extras
                } else {
                    hashMapOf()
                }
            )

            val issue = gitHubRepository.createIssue(
                title,
                descriptionMarkdown,
                assignees,
                labels
            )

            NotificationUtils.sendNotification(this, issue)
            finish()
        }
    }

    private fun execute(request: suspend () -> Unit) {
        lifecycleScope.launch {
            loadingLayout.isVisible = true
            try {
                request.invoke()
                loadingLayout.isVisible = false
            } catch (exception: Exception) {
                findViewById<View>(android.R.id.content)?.let {
                    Snackbar.make(
                        it,
                        it.context.getString(R.string.common_error),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                loadingLayout.isVisible = false
            }
        }
    }

    companion object {
        const val ARG_SCREENSHOT = "screenshot"
        const val ARG_CONFIG = "arg_config"
    }
}
