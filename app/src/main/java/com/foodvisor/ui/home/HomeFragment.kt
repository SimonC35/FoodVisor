package com.foodvisor.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodvisor.FoodvisorApplication
import com.foodvisor.databinding.FragmentHomeBinding
import com.foodvisor.ui.ViewModelFactory
import com.foodvisor.ui.adapters.RestaurantAdapter
import com.foodvisor.ui.detail.RestaurantDetailActivity
import com.foodvisor.utils.hide
import com.foodvisor.utils.show
import com.foodvisor.utils.showToast

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        val app = requireActivity().application as FoodvisorApplication
        ViewModelFactory(app.restaurantRepository)
    }

    private lateinit var adapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
        setupSwipeRefresh()
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

        binding.rvRestaurants.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->
            adapter.submitList(restaurants)

            if (restaurants.isEmpty()) {
                binding.tvEmpty.show()
                binding.rvRestaurants.hide()
            } else {
                binding.tvEmpty.hide()
                binding.rvRestaurants.show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                requireContext().showToast(it)
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        // Rafraîchir quand on revient à l'écran
        viewModel.loadRestaurants()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}