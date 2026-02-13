package com.foodvisor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.foodvisor.data.local.PreferencesManager
import com.foodvisor.data.repository.RestaurantRepository
import com.foodvisor.ui.detail.RestaurantDetailViewModel
import com.foodvisor.ui.favorites.FavoritesViewModel
import com.foodvisor.ui.home.HomeViewModel
import com.foodvisor.ui.profile.ProfileViewModel
import com.foodvisor.ui.search.SearchViewModel

class ViewModelFactory(
    private val restaurantRepository: RestaurantRepository,
    private val preferencesManager: PreferencesManager? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(restaurantRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(restaurantRepository) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(restaurantRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(preferencesManager!!) as T
            }
            modelClass.isAssignableFrom(RestaurantDetailViewModel::class.java) -> {
                RestaurantDetailViewModel(restaurantRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}