package com.karacca.beetle.ui.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author karacca
 * @date 28.07.2022
 */

internal class HorizontalItemDecorator(private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildLayoutPosition(view)
        val isFirstItem = position == 0
        val isLastItem = position == (parent.adapter?.itemCount ?: 1) - 1

        outRect.left = if (!isFirstItem) {
            spacing
        } else {
            0
        }

        outRect.right = spacing

    }
}
