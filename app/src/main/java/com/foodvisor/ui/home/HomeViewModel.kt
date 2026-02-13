package com.foodvisor.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodvisor.data.model.Restaurant
import com.foodvisor.data.repository.RestaurantRepository
import com.foodvisor.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RestaurantRepository
) : BaseViewModel() {

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    init {
        loadRestaurants()
    }

    fun loadRestaurants(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            setLoading(true)
            clearError()

            val result = repository.getAllRestaurants(forceRefresh)
            result.fold(
                onSuccess = { restaurants ->
                    _restaurants.value = restaurants
                    setLoading(false)
                },
                onFailure = { exception ->
                    setError(exception.message ?: "Erreur de chargement")
                    setLoading(false)
                }
            )
        }
    }

    fun toggleFavorite(restaurant: Restaurant) {
        viewModelScope.launch {
            val result = repository.toggleFavorite(restaurant)
            result.fold(
                onSuccess = {
                    // Rafraîchir la liste
                    loadRestaurants()
                },
                onFailure = { exception ->
                    setError(exception.message ?: "Erreur de mise à jour")
                }
            )
        }
    }

    fun refresh() {
        loadRestaurants(forceRefresh = true)
    }
}