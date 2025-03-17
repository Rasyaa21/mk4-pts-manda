package com.example.mk4b_pas

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class LoginActivity : AppCompatActivity() {
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        userRepository = UserRepository(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.LoginBt).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEt).text.toString()
            val password = findViewById<EditText>(R.id.passwordEt).text.toString()
            val signUpBt = findViewById<TextView>(R.id.goToLoginTv)

            if (userRepository.checkUserCredentials(email, password)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
            signUpBt.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}