package com.foodvisor.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodvisor.FoodvisorApplication
import com.foodvisor.R
import com.foodvisor.databinding.FragmentSearchBinding
import com.foodvisor.ui.ViewModelFactory
import com.foodvisor.ui.adapters.RestaurantAdapter
import com.foodvisor.ui.detail.RestaurantDetailActivity
import com.foodvisor.utils.hide
import com.foodvisor.utils.hideKeyboard
import com.foodvisor.utils.show
import com.foodvisor.utils.showToast
import kotlinx.coroutines.*

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels {
        val app = requireActivity().application as FoodvisorApplication
        ViewModelFactory(app.restaurantRepository)
    }

    private lateinit var adapter: RestaurantAdapter
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFilters()
        setupSearch()
        setupObservers()

        // Recherche initiale
        viewModel.searchRestaurants()
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

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }
    }

    private fun setupFilters() {
        // Cuisine
        val cuisines = listOf("Toutes", "Française", "Française Contemporaine", "Méditerranéenne")
        val cuisineAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cuisines)
        cuisineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCuisine.adapter = cuisineAdapter

        // Étoiles
        val stars = listOf("Toutes", "1+", "2+", "3")
        val starsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stars)
        starsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStars.adapter = starsAdapter

        // Prix
        val prices = listOf("Tous prix", "< 100€", "< 200€", "< 300€")
        val priceAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, prices)
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPrice.adapter = priceAdapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            searchJob?.cancel()
            searchJob = CoroutineScope(Dispatchers.Main).launch {
                delay(500) // Debounce
                performSearch()
            }
        }

        binding.btnSearch.setOnClickListener {
            it.hideKeyboard()
            performSearch()
        }
    }

    private fun performSearch() {
        val query = binding.etSearch.text.toString().trim()
        val cuisine = binding.spinnerCuisine.selectedItem.toString()
        val stars = binding.spinnerStars.selectedItem.toString()
        val price = binding.spinnerPrice.selectedItem.toString()

        val cuisineFilter = if (cuisine == "Toutes") null else cuisine
        val starsFilter = when (stars) {
            "1+" -> 1
            "2+" -> 2
            "3" -> 3
            else -> null
        }
        val priceFilter = when (price) {
            "< 100€" -> 100.0
            "< 200€" -> 200.0
            "< 300€" -> 300.0
            else -> null
        }

        viewModel.searchRestaurants(
            query = query.ifEmpty { null },
            cuisine = cuisineFilter,
            etoiles = starsFilter,
            prixMax = priceFilter
        )
    }

    private fun setupObservers() {
        viewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->
            adapter.submitList(restaurants)

            if (restaurants.isEmpty()) {
                binding.tvEmpty.show()
                binding.rvSearchResults.hide()
            } else {
                binding.tvEmpty.hide()
                binding.rvSearchResults.show()
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
        searchJob?.cancel()
        _binding = null
    }
}