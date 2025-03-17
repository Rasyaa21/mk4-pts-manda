package com.example.mk4b_pas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mk4b_pas.databinding.FragmentNotesBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {
    private lateinit var fabAddNote: FloatingActionButton

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var fabAddTask: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notes = listOf(
            "Meeting Notes",
            "Grocery List",
            "Project Ideas"
        )

        binding.recyclerViewNotes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SimpleStringAdapter(notes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
