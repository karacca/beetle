package com.karacca.beetle.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.textview.MaterialTextView
import com.karacca.beetle.R
import com.karacca.beetle.data.model.Label

/**
 * @author karacca
 * @date 28.07.2022
 */

internal class LabelAdapter(
    private val listener: (Int) -> Unit
) : ListAdapter<Label, LabelAdapter.ViewHolder>(LabelDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_label,
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

        fun bind(label: Label) {
            rootLayout.isSelected = label.selected
            textView.text = label.name
            imageView.load(Color.parseColor("#${label.color}").toDrawable()) {
                transformations(RoundedCornersTransformation(24f))
            }
        }
    }

    object LabelDiff : DiffUtil.ItemCallback<Label>() {
        override fun areItemsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Label, newItem: Label): Boolean {
            return false
        }
    }
}
