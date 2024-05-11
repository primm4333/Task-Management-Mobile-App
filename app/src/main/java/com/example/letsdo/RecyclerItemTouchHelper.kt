package com.example.letsdo

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.letsdo.Adapter.ToDoAdapter
import com.example.letsdo.R

class RecyclerItemTouchHelper(private val adapter: ToDoAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder(adapter.context)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this Task?")
                .setPositiveButton("Confirm") { dialog, _ ->
                    adapter.deleteItem(position)
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    adapter.notifyItemChanged(viewHolder.adapterPosition)
                }
                .create()
                .show()
        } else {
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val icon: Drawable?
        val background: ColorDrawable

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        if (dX > 0) {
            icon = ContextCompat.getDrawable(adapter.context, R.drawable.baseline_edit)
            background = ColorDrawable(ContextCompat.getColor(adapter.context, R.color.colorPrimaryDark))
        } else {
            icon = ContextCompat.getDrawable(adapter.context, R.drawable.baseline_delete_)
            background = ColorDrawable(Color.RED)
        }

        icon?.let {
            val iconMargin = (itemView.height - it.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemView.height - it.intrinsicHeight) / 2
            val iconBottom = iconTop + it.intrinsicHeight

            if (dX > 0) { // Swiping to the right
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + it.intrinsicWidth
                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(itemView.left, itemView.top,
                    itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom)
            } else if (dX < 0) { // Swiping to the left
                val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                it.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom)
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0)
            }

            background.draw(c)
            it.draw(c)
        }
    }
}
