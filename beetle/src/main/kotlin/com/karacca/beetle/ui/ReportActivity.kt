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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.karacca.beetle.R
import com.karacca.beetle.data.repository.GitHubRepository
import com.karacca.beetle.data.model.Collaborator
import com.karacca.beetle.data.model.Image
import com.karacca.beetle.data.model.Label
import com.karacca.beetle.data.repository.ImageRepository
import com.karacca.beetle.ui.adapter.CollaboratorAdapter
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

internal class ReportActivity : AppCompatActivity(), TextWatcher {

    private lateinit var screenshot: Uri
    private lateinit var customData: Bundle

    private lateinit var gitHubRepository: GitHubRepository
    private lateinit var imageRepository: ImageRepository

    private lateinit var toolbar: MaterialToolbar
    private lateinit var imageView: AppCompatImageView
    private lateinit var screenshotCardView: MaterialCardView
    private lateinit var logsCardView: MaterialCardView
    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var loadingLayout: ConstraintLayout
    private lateinit var checkBoxLogs: MaterialCheckBox
    private lateinit var checkBoxScreenshot: MaterialCheckBox

    private lateinit var assigneesRecyclerView: RecyclerView
    private lateinit var labelsRecyclerView: RecyclerView

    private lateinit var collaboratorAdapter: CollaboratorAdapter
    private lateinit var labelAdapter: LabelAdapter

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        screenshot = intent.extras!!.getParcelable(ARG_SCREENSHOT)!!
        customData = intent.extras!!.getParcelable(ARG_CUSTOM_DATA)!!

        val organization = intent.extras!!.getString(ARG_ORGANIZATION)!!
        val repository = intent.extras!!.getString(ARG_REPOSITORY)!!

        toolbar = findViewById(R.id.toolbar)
        imageView = findViewById(R.id.image_view_screenshot)
        screenshotCardView = findViewById(R.id.card_view_screenshot)
        logsCardView = findViewById(R.id.card_view_logs)
        titleEditText = findViewById(R.id.edit_text_title)
        descriptionEditText = findViewById(R.id.edit_text_description)
        assigneesRecyclerView = findViewById(R.id.recycler_view_assignees)
        labelsRecyclerView = findViewById(R.id.recycler_view_labels)
        loadingLayout = findViewById(R.id.layout_loading)
        checkBoxLogs = findViewById(R.id.checkbox_logs)
        checkBoxScreenshot = findViewById(R.id.checkbox_screenshot)

        gitHubRepository = GitHubRepository(getPrivateKey(application), organization, repository)
        imageRepository = ImageRepository()
        execute {
            setCollaborators(gitHubRepository.getCollaborators())
            setLabels(gitHubRepository.getLabels())
        }

        toolbar.setNavigationOnClickListener { finish() }
        imageView.setImageURI(screenshot)
        screenshotCardView.setOnClickListener {
            openEditActivity.launch(screenshot)
        }

        titleEditText.addTextChangedListener(this)
        descriptionEditText.addTextChangedListener(this)

        toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener if (it.itemId == R.id.send) {
                createIssue()
                true
            } else {
                false
            }
        }

        collaboratorAdapter = CollaboratorAdapter {
            val collaborators = collaboratorAdapter.currentList
            collaborators[it].selected = !collaborators[it].selected
            collaboratorAdapter.submitList(collaborators)
        }

        assigneesRecyclerView.apply {
            addItemDecoration(HorizontalItemDecorator(8))
            adapter = collaboratorAdapter
            layoutManager = LinearLayoutManager(
                this@ReportActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }

        labelAdapter = LabelAdapter {
            val labels = labelAdapter.currentList
            labels[it].selected = !labels[it].selected
            labelAdapter.submitList(labels)
        }

        labelsRecyclerView.apply {
            addItemDecoration(HorizontalItemDecorator(8))
            adapter = labelAdapter
            layoutManager = LinearLayoutManager(
                this@ReportActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
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

    private fun setCollaborators(collaborators: List<Collaborator>) {
        collaboratorAdapter.submitList(collaborators)
    }

    private fun setLabels(labels: List<Label>) {
        labelAdapter.submitList(labels)
    }

    private fun createIssue() {
        val title = titleEditText.text?.toString() ?: ""
        val description = descriptionEditText.text?.toString() ?: ""
        val assignees = collaboratorAdapter.currentList.filter { it.selected }.map { it.login }
        val labels = labelAdapter.currentList.filter { it.selected }.map { it.name }

        execute {
            val image: Image? = if (checkBoxScreenshot.isChecked) {
                imageRepository.uploadImage(screenshot)
            } else {
                null
            }

            val descriptionMarkdown = MarkdownUtils.createDescription(
                this,
                description,
                image?.image?.url,
                if (checkBoxLogs.isChecked) {
                    DeviceUtils.getDeviceData(this)
                } else {
                    null
                },
                customData
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
                        it.context.getString(R.string.error),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                loadingLayout.isVisible = false
            }
        }
    }

    companion object {
        const val ARG_SCREENSHOT = "screenshot"
        const val ARG_CUSTOM_DATA = "custom_data"
        const val ARG_ORGANIZATION = "organization"
        const val ARG_REPOSITORY = "repository"
    }
}
