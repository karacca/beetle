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

package com.karacca.beetle.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.textview.MaterialTextView
import com.karacca.beetle.R
import com.karacca.beetle.data.model.Collaborator

/**
 * @author karacca
 * @date 27.07.2022
 */

internal class AssigneeAdapter(
    private val listener: (Int) -> Unit
) : ListAdapter<Collaborator, AssigneeAdapter.ViewHolder>(CollaboratorDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_assignee,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { listener.invoke(position) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rootLayout = itemView.findViewById<LinearLayoutCompat>(R.id.layout_root)
        private val textView = itemView.findViewById<MaterialTextView>(R.id.text_view)
        private val imageView = itemView.findViewById<AppCompatImageView>(R.id.image_view)

        fun bind(collaborator: Collaborator) {
            rootLayout.isSelected = collaborator.selected
            textView.text = collaborator.login
            imageView.load(collaborator.avatarUrl) {
                transformations(RoundedCornersTransformation(24f))
            }
        }
    }

    object CollaboratorDiff : DiffUtil.ItemCallback<Collaborator>() {
        override fun areItemsTheSame(oldItem: Collaborator, newItem: Collaborator): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Collaborator, newItem: Collaborator): Boolean {
            return false
        }
    }
}
