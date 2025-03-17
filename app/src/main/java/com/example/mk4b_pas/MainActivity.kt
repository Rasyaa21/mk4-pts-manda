package com.example.mk4b_pas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mk4b_pas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Muat fragment awal (TodayFragment)
        if (savedInstanceState == null) {
            loadFragment(TodayFragment())
        }

        // BottomNavigationView Listener
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment? = when (item.itemId) {
                R.id.bottom_today -> TodayFragment()
                R.id.bottom_upcoming -> UpcomingFragment()
                R.id.bottom_search -> SearchFragment()
                R.id.bottom_notes -> NotesFragment()
                else -> null
            }
            selectedFragment?.let {
                loadFragment(it)
            } ?: false
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }
}
