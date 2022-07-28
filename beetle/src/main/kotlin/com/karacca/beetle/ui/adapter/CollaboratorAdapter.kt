package com.karacca.beetle.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView
import com.karacca.beetle.R
import com.karacca.beetle.network.model.Collaborator

/**
 * @author karacca
 * @date 27.07.2022
 */

internal class CollaboratorAdapter(
    private val collaborators: List<Collaborator>,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<CollaboratorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_collaborator, parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(collaborators[position])
        holder.itemView.setOnClickListener { listener.invoke(position) }
    }

    override fun getItemCount() = collaborators.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(collaborator: Collaborator) {
            itemView.findViewById<LinearLayoutCompat>(R.id.layout_root).apply {
                isSelected = collaborator.selected
            }

            itemView.findViewById<MaterialTextView>(R.id.text_view).text = collaborator.login
            itemView.findViewById<AppCompatImageView>(R.id.image_view)
                .load(collaborator.avatarUrl) {
                    transformations(RoundedCornersTransformation(24f))
                }
        }
    }
}
