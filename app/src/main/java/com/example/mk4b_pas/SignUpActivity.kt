package com.example.mk4b_pas

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mk4b_pas.MainActivity
import com.example.mk4b_pas.R
import com.example.mk4b_pas.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var loadingDialog: Dialog
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "SignUpActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Konfigurasi Google Sign-In
        configureGoogleSignIn()

        // Tombol Daftar dengan Email & Password
        binding.signUpBt.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            val name = binding.nameEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isValidPassword(password)) {
                signUpWithEmail(name, email, password)
            }
        }

        // Tombol Sign-In dengan Google
        binding.googleSignUpBt.setOnClickListener {
            signInWithGoogle()
        }

        // Navigasi ke Login Page
        binding.goToLoginTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun configureGoogleSignIn() {
        try {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
        } catch (e: Exception) {
            Log.e(TAG, "Error configuring Google Sign-In: ${e.message}")
            Toast.makeText(this, "Gagal memuat konfigurasi Google Sign-In", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUpWithEmail(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Log.d(TAG, "Email verifikasi telah dikirim ke $email")
                            saveUserData(name, email, user.uid)

                            Toast.makeText(
                                this,
                                "Registrasi berhasil! Silakan cek email untuk verifikasi.",
                                Toast.LENGTH_LONG
                            ).show()

                            auth.signOut()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Log.e(TAG, "Gagal mengirim email verifikasi", verificationTask.exception)
                            Toast.makeText(
                                this,
                                "Gagal mengirim email verifikasi.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    Log.w(TAG, "Registrasi gagal", task.exception)
                    Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // Fungsi Sign-In dengan Google
    private fun signInWithGoogle() {
        try {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting Google Sign-In: ${e.message}")
            Toast.makeText(this, "Gagal memulai Google Sign-In", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher untuk menangani hasil Sign-In Google
    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val data = result.data
            if (data == null) {
                Log.w(TAG, "Google sign in failed: No data returned")
                Toast.makeText(this, "Login Google gagal: Tidak ada data yang dikembalikan", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle: ${account.id}")
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d(TAG, "Google sign in was canceled by user")
                        Toast.makeText(this, "Login Google dibatalkan", Toast.LENGTH_SHORT).show()
                    }
                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.e(TAG, "Network error during Google sign in")
                        Toast.makeText(this, "Gagal terhubung ke Google. Periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show()
                    }
                    CommonStatusCodes.DEVELOPER_ERROR -> {
                        Log.e(TAG, "Developer error with Google Sign-In configuration")
                        Toast.makeText(this, "Terjadi kesalahan konfigurasi Google Sign-In", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Log.w(TAG, "Google sign in failed with code: ${e.statusCode}", e)
                        Toast.makeText(this, "Login Google gagal (kode: ${e.statusCode})", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error in Google Sign-In result", e)
            Toast.makeText(this, "Terjadi kesalahan tak terduga saat login Google", Toast.LENGTH_SHORT).show()
        }
    }

    // Autentikasi Firebase dengan akun Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserData(it.displayName ?: "User", it.email ?: "", it.uid)
                    }
                    Toast.makeText(this, "Login dengan Google berhasil!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "Sign in with Google failed", task.exception)
                    Toast.makeText(this, "Login Google gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(name: String, email: String, uid: String) {
        val userData = hashMapOf(
            "uid" to uid,
            "email" to email,
            "name" to name
        )

        db.collection("USERS").document(uid)
            .set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "Data pengguna disimpan: $uid")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Gagal menyimpan data pengguna", e)
            }
    }

    private fun isValidPassword(password: String): Boolean {
        val errorMessage = StringBuilder()
        if (password.length < 8) errorMessage.append("Password minimal 8 karakter.\n")
        if (!password.any { it.isDigit() }) errorMessage.append("Password harus mengandung angka.\n")
        if (!password.any { it.isUpperCase() }) errorMessage.append("Password harus mengandung huruf besar.\n")
        if (!password.any { !it.isLetterOrDigit() }) errorMessage.append("Password harus mengandung simbol.\n")

        return if (errorMessage.isNotEmpty()) {
            Toast.makeText(this, errorMessage.toString(), Toast.LENGTH_LONG).show()
            false
        } else {
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}