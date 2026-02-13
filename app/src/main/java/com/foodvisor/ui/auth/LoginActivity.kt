package com.foodvisor.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.foodvisor.FoodvisorApplication
import com.foodvisor.MainActivity
import com.foodvisor.databinding.ActivityLoginBinding
import com.foodvisor.utils.hideKeyboard
import com.foodvisor.utils.showToast
import kotlinx.coroutines.launch
import java.util.UUID

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val app by lazy { application as FoodvisorApplication }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                handleLogin()
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            tvSkipLogin.setOnClickListener {
                handleGuestLogin()
            }
        }
    }

    private fun handleLogin() {
        binding.apply {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validation
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

            root.hideKeyboard()

            // Authentification simulée
            lifecycleScope.launch {
                try {
                    val userId = UUID.randomUUID().toString()
                    val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
                    app.preferencesManager.saveUserData(userId, email, name)

                    showToast("Connexion réussie!")
                    navigateToMain()
                } catch (e: Exception) {
                    showToast("Erreur: ${e.message}")
                }
            }
        }
    }

    private fun handleGuestLogin() {
        lifecycleScope.launch {
            val guestId = "guest_${UUID.randomUUID()}"
            app.preferencesManager.saveUserData(guestId, "guest@foodvisor.com", "Invité")
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}