package com.foodvisor.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.foodvisor.FoodvisorApplication
import com.foodvisor.databinding.FragmentProfileBinding
import com.foodvisor.ui.ViewModelFactory
import com.foodvisor.ui.auth.LoginActivity
import com.foodvisor.utils.showToast
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        val app = requireActivity().application as FoodvisorApplication
        ViewModelFactory(app.restaurantRepository, app.preferencesManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.userName.collect { name ->
                binding.tvUserName.text = name
            }
        }

        lifecycleScope.launch {
            viewModel.userEmail.collect { email ->
                binding.tvUserEmail.text = email
            }
        }
    }

    private fun setupListeners() {
        binding.apply {
            btnEditProfile.setOnClickListener {
                showEditNameDialog()
            }

            btnChangePassword.setOnClickListener {
                requireContext().showToast("Fonctionnalité à venir")
            }

            btnNotifications.setOnClickListener {
                requireContext().showToast("Fonctionnalité à venir")
            }

            btnAbout.setOnClickListener {
                showAboutDialog()
            }

            btnLogout.setOnClickListener {
                showLogoutDialog()
            }
        }
    }

    private fun showEditNameDialog() {
        val input = EditText(requireContext()).apply {
            setText(viewModel.userName.value)
            setPadding(50, 30, 50, 30)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Modifier le nom")
            .setView(input)
            .setPositiveButton("Enregistrer") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    viewModel.updateUserName(newName)
                    requireContext().showToast("Nom mis à jour")
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("À propos")
            .setMessage("Foodvisor v1.0\n\nDécouvrez les meilleurs restaurants gastronomiques.\n\nDéveloppé avec ❤️")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Déconnexion")
            .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
            .setPositiveButton("Oui") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun performLogout() {
        viewModel.logout()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}