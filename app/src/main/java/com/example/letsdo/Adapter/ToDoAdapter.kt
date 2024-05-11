package com.example.letsdo.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.letsdo.Model.ToDoModel
import com.example.letsdo.AddNewTask
import com.example.letsdo.MainActivity
import com.example.letsdo.R
import com.example.letsdo.Utils.DatabaseHandler

class ToDoAdapter(private val db: DatabaseHandler, private val activity: MainActivity) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    val context: Context
        get() = activity
    private var todoList: List<ToDoModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        db.openDatabase()

        val item = todoList[position]
        holder.task.text = item.task
        holder.task.isChecked = item.status != 0
        holder.task.setOnCheckedChangeListener { buttonView, isChecked ->
            val newStatus = if (isChecked) 1 else 0
            db.updateStatus(item.id, newStatus)
        }
    }

    private fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun setTasks(todoList: List<ToDoModel>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val item = todoList[position]
        db.deleteTask(item.id)
        todoList = todoList.toMutableList().apply { removeAt(position) }
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = todoList[position]
        val bundle = Bundle().apply {
            putInt("id", item.id)
            putString("task", item.task)
        }
        val fragment = AddNewTask.newInstance().apply {
            arguments = bundle
        }
        fragment.show(activity.supportFragmentManager, AddNewTask.TAG)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val task: CheckBox = itemView.findViewById(R.id.todoCheckBox)
    }
}
