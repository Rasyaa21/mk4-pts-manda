package com.example.mk4b_pas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mk4b_pas.databinding.FragmentUpcomingBinding
import com.google.android.material.datepicker.MaterialDatePicker

class UpcomingFragment : Fragment() {


    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .build()

        binding.fabAddEvent.setOnClickListener {
            picker.show(parentFragmentManager, "tag")
        }

        picker.addOnPositiveButtonClickListener { selection ->
            // Tampilkan tanggal yang dipilih
            Toast.makeText(context, "Selected: $selection", Toast.LENGTH_SHORT).show()
        }


        val upcomingTasks = listOf(
            TasksAdapter.Task("Prepare Presentation", "Tomorrow"),
            TasksAdapter.Task("Project Deadline", "Next Monday"),
            TasksAdapter.Task("Family Gathering", "Next Friday")
        )

        binding.recyclerViewUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TasksAdapter(upcomingTasks)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
