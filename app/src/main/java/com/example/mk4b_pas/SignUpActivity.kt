package com.example.mk4b_pas

import DatabaseHelper
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mk4b_pas.DataClass.User

class SignUpActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        userRepository = UserRepository(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.signUpBt).setOnClickListener {
            val name = findViewById<EditText>(R.id.nameEt).text.toString()
            val email = findViewById<EditText>(R.id.emailEt).text.toString()
            val password = findViewById<EditText>(R.id.passwordEt).text.toString()
            val LoginBt = findViewById<TextView>(R.id.goToLoginTv)

            if (userRepository.checkUserExists(email)) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(name = name, email = email, password = password)
                userRepository.addUser(user)
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            LoginBt.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}