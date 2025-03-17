package com.example.mk4b_pas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodayFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddTask: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)

        recyclerView = view.findViewById(R.id.recycler_today_tasks)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TasksAdapter(getTodayTasks())

        fabAddTask = view.findViewById(R.id.fab_add_task)
        fabAddTask.setOnClickListener {
            // Aksi untuk menambahkan task
            Toast.makeText(context, "Add Task Clicked", Toast.LENGTH_SHORT).show()
        }
        return view
    }


    private fun getTodayTasks(): List<TasksAdapter.Task> {
        return listOf(
            TasksAdapter.Task("Morning Meeting", "09:00 AM"),
            TasksAdapter.Task("Code Review", "11:00 AM"),
            TasksAdapter.Task("Lunch Break", "12:30 PM"),
            TasksAdapter.Task("Team Sync", "03:00 PM")
        )
    }
}
