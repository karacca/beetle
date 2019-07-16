package com.karacce.beetle.network

import com.karacce.beetle.data.Agent
import com.karacce.beetle.data.Label
import com.karacce.beetle.data.User
import com.karacce.beetle.data.remote.Result
import com.karacce.beetle.network.service.AzureService
import com.karacce.beetle.network.service.GithubService
import com.karacce.beetle.network.service.GitlabService
import com.karacce.beetle.network.service.VisualStudioService
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream


/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

class ApiWrapper(private val agent: Agent) {

    private var azure: AzureService? = null
    private var visualStudio: VisualStudioService? = null
    private var github: GithubService? = null
    private var gitlab: GitlabService? = null

    init {
        when(agent.type) {
            Agent.Type.AZURE -> {
                azure = NetworkClient.createAzureService(agent.namespace!!, agent.project as String, agent.token)
                visualStudio = NetworkClient.createVisualStudioService(agent.namespace, agent.token)
            }

            Agent.Type.GITHUB -> {
                github = NetworkClient.createGitHubService(agent.namespace!!, agent.project as String, agent.token)
            }

            Agent.Type.GITLAB -> {
                gitlab = NetworkClient.createGitLabService(agent.project as Int, agent.token)
            }
        }
    }

    fun users(): Observable<List<User>> {
        when(agent.type) {
            Agent.Type.AZURE -> {
                return visualStudio!!.users("msa").map { response ->
                    response.value.map { user ->
                        User(user.name, user.descriptor, user.avatar)
                    }
                }
            }

            Agent.Type.GITHUB -> {
                return github!!.collaborators().map { users ->
                    users.map { user ->
                        User(user.login, user.login, user.avatar)
                    }
                }
            }

            Agent.Type.GITLAB -> {
                return gitlab!!.members().map { users ->
                    users.map { user ->
                        User(user.name, user.id, user.avatar)
                    }
                }
            }
        }
    }

    fun labels(): Observable<List<Label>>? {
        when(agent.type) {
            Agent.Type.AZURE -> return null

            Agent.Type.GITHUB -> {
                return github!!.labels().map { labels ->
                    labels.map { label ->
                        Label(label.name, "#${label.color}", label.name)
                    }
                }
            }

            Agent.Type.GITLAB -> {
                return gitlab!!.labels().map { labels ->
                    labels.map { label ->
                        Label(label.name, label.color, label.name)
                    }
                }
            }
        }
    }

    fun submit(result: Result, listener: ((Int?, String?) -> Unit)) {
        when(agent.type) {
            Agent.Type.AZURE -> {
                if (result.attachmentPath != null) {
                    val inputStream = FileInputStream(File(result.attachmentPath))
                    val buf: ByteArray
                    buf = ByteArray(inputStream.available())
                    while (inputStream.read(buf) != -1);

                    azure!!.uploadPhoto("screenshot.png", RequestBody.create(MediaType.parse("application/octet-stream"), buf))
                        .subscribe({
                            result.attachmentUrl = it.url
                            azure!!.createWorkItem("application/json-patch+json", "Issue", result.azureWorkItem())
                                .subscribe({ response ->
                                    listener.invoke(response.id, response.url)
                                },{ listener.invoke(null, null) })

                        }, { listener.invoke(null, null) })

                }else {
                    azure!!.createWorkItem("application/json-patch+json", "Issue", result.azureWorkItem())
                        .subscribe({ response ->
                            listener.invoke(response.id, response.url)
                        },{ listener.invoke(null, null) })
                }
            }

            Agent.Type.GITHUB -> {
                github!!.issue(result.githubIssue())
                    .subscribe({ response ->
                        listener.invoke(response.number, response.url)
                    },{ listener.invoke(null, null) })
            }

            Agent.Type.GITLAB -> {
                if (result.attachmentPath != null) {
                    val file = File(result.attachmentPath)
                    gitlab!!.upload(MultipartBody.Part.createFormData("file", "screenshot.png",
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))).subscribe({
                        result.attachmentUrl = it.markdown

                        gitlab!!.issues(result.gitlabIssue())
                            .subscribe({ response ->
                                listener.invoke(response.id, response.url)
                            }, { listener.invoke(null, null) })

                    },{ listener.invoke(null, null) })
                }else {

                    gitlab!!.issues(result.gitlabIssue())
                        .subscribe({ response ->
                            listener.invoke(response.id, response.url)
                        }, { listener.invoke(null, null) })

                }
            }
        }
    }

}