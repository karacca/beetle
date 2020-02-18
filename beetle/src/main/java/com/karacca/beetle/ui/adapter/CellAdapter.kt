package com.karacca.beetle.ui.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.karacca.beetle.R
import com.karacca.beetle.data.Cell
import com.karacca.beetle.data.Label
import com.karacca.beetle.data.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.viewholder_cell.view.*

/**
 * @user: omerkaraca
 * @date: 2019-07-12
 */

class CellAdapter(private val items: List<Cell>,
                  private val token: String? = null,
                  private val listener: (Int) -> Unit): RecyclerView.Adapter<CellAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.viewholder_cell,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(items[position]) {
            is User -> holder.bind(items[position] as User, token ?: "")
            is Label -> holder.bind(items[position] as Label)
        }
        holder.root.setOnClickListener { listener.invoke(position) }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val root: ConstraintLayout = view.root
        private val image: CircleImageView = view.image
        private val name: AppCompatTextView = view.name

        fun bind(user: User, token: String) {
            root.setBackgroundResource(if (user.selected) R.drawable.bg_cell_selected else R.drawable.bg_cell)
            name.text = user.name
            Glide.with(itemView)
                .load(GlideUrl(user.avatar, LazyHeaders.Builder().addHeader("Authorization", token).build()))
                .into(image)
        }

        fun bind(label: Label) {
            root.setBackgroundResource(if (label.selected) R.drawable.bg_cell_selected else R.drawable.bg_cell)
            image.setImageDrawable(ColorDrawable(Color.parseColor(label.color)))
            name.text = label.label
        }
    }

}