package com.karacce.beetle.data.remote.azure

import com.google.gson.annotations.SerializedName
import com.karacce.beetle.data.Label
import com.karacce.beetle.data.User

/**
 * @user: omerkaraca
 * @date: 2019-07-14
 */

class WorkItemComposer {

    data class Document(@SerializedName("from") val from: String?,
                        @SerializedName("op") val op: String,
                        @SerializedName("path") val path: String,
                        @SerializedName("value") val value: Any)

    class Builder {
        private var body: ArrayList<Document> = arrayListOf()

        fun title(title: String): Builder {
            body.add(Document(null, "add", "/fields/System.Title", title))
            return this
        }

        fun description(description: String): Builder {
            body.add(Document(null, "add", "/fields/System.Description", description))
            return this
        }

        fun labels(labels: List<Label>): Builder {
            body.add(Document(null, "add", "/fields/System.Tags", labels.joinToString { it.descriptor }))
            return this
        }

        fun assignee(user: User): Builder {
            body.add( Document(null, "add", "/fields/System.AssignedTo", HashMap<String, Any>().apply {
                put("descriptor", user.descriptor)
            }))
            return this
        }

        fun attachment(attachmentUrl: String): Builder {
            body.add(Document(null, "add", "/relations/-", HashMap<String, Any>().apply {
                put("rel", "AttachedFile")
                put("url", attachmentUrl)
            }))
            return this
        }

        fun system(info: String): Builder {
            body.add(Document(null, "add", "/fields/Microsoft.VSTS.TCM.SystemInfo", info))
            return this
        }

        fun reproSteps(value: String): Builder {
            body.add(Document(null, "add", "/fields/Microsoft.VSTS.TCM.ReproSteps", value))
            return this
        }

        fun build(): List<Document> {
            return body
        }

    }

}