package com.foodvisor.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.foodvisor.FoodvisorApplication
import com.foodvisor.MainActivity
import com.foodvisor.databinding.ActivityRegisterBinding
import com.foodvisor.utils.hideKeyboard
import com.foodvisor.utils.showToast
import kotlinx.coroutines.launch
import java.util.UUID

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val app by lazy { application as FoodvisorApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                handleRegister()
            }

            tvLogin.setOnClickListener {
                finish()
            }
        }
    }

    private fun handleRegister() {
        binding.apply {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            // Validation
            if (name.isEmpty()) {
                etName.error = "Nom requis"
                return
            }

            if (email.isEmpty()) {
                etEmail.error = "Email requis"
                return
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Email invalide"
                return
            }

            if (password.isEmpty()) {
                etPassword.error = "Mot de passe requis"
                return
            }

            if (password.length < 6) {
                etPassword.error = "Minimum 6 caractères"
                return
            }

            if (password != confirmPassword) {
                etConfirmPassword.error = "Les mots de passe ne correspondent pas"
                return
            }

            root.hideKeyboard()

            // Inscription simulée
            lifecycleScope.launch {
                try {
                    val userId = UUID.randomUUID().toString()
                    app.preferencesManager.saveUserData(userId, email, name)

                    showToast("Inscription réussie!")
                    navigateToMain()
                } catch (e: Exception) {
                    showToast("Erreur: ${e.message}")
                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}