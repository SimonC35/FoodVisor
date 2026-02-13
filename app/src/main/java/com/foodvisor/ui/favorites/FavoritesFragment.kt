package com.foodvisor.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodvisor.FoodvisorApplication
import com.foodvisor.databinding.FragmentFavoritesBinding
import com.foodvisor.ui.ViewModelFactory
import com.foodvisor.ui.adapters.RestaurantAdapter
import com.foodvisor.ui.detail.RestaurantDetailActivity
import com.foodvisor.utils.hide
import com.foodvisor.utils.show
import com.foodvisor.utils.showToast

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels {
        val app = requireActivity().application as FoodvisorApplication
        ViewModelFactory(app.restaurantRepository)
    }

    private lateinit var adapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    private fun setupRecyclerView() {
        adapter = RestaurantAdapter(
            onItemClick = { restaurant ->
                val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
                intent.putExtra(RestaurantDetailActivity.EXTRA_RESTAURANT_ID, restaurant.id)
                startActivity(intent)
            },
            onFavoriteClick = { restaurant ->
                viewModel.toggleFavorite(restaurant)
            }
        )

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoritesFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            adapter.submitList(favorites)

            if (favorites.isEmpty()) {
                binding.tvEmpty.show()
                binding.rvFavorites.hide()
            } else {
                binding.tvEmpty.hide()
                binding.rvFavorites.show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                requireContext().showToast(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}