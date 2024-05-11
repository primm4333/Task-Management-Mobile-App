package com.example.letsdo

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.letsdo.Adapter.ToDoAdapter
import com.example.letsdo.Model.ToDoModel
import com.example.letsdo.Utils.DatabaseHandler

class MainActivity : AppCompatActivity(), DialogCloseListener {
    private lateinit var db: DatabaseHandler
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private lateinit var fab: FloatingActionButton
    private var taskList: MutableList<ToDoModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        db = DatabaseHandler(this)
        db.openDatabase()

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksAdapter = ToDoAdapter(db, this)
        tasksRecyclerView.adapter = tasksAdapter

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        fab = findViewById(R.id.fab)

        taskList = db.getAllTasks().toMutableList()
        taskList.reverse()

        tasksAdapter.setTasks(taskList)

        fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }
    }

    override fun handleDialogClose(dialog: DialogInterface?) {
        // Update the task list from the database
        taskList = db.getAllTasks().toMutableList()
        taskList.reverse()
        tasksAdapter.setTasks(taskList)
        tasksAdapter.notifyDataSetChanged()

        // Show a toast message indicating that the dialog has been closed
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show()
    }
}