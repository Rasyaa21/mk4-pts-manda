package com.example.mk4b_pas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mk4b_pas.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Simulasi data hasil pencarian
        val searchResults = listOf(
            TasksAdapter.Task("Learn Kotlin", "Today"),
            TasksAdapter.Task("Organize Desk", "Tomorrow"),
            TasksAdapter.Task("Buy Groceries", "Weekend")
        )

        // Setup RecyclerView
        binding.recyclerViewSearch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TasksAdapter(searchResults)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
