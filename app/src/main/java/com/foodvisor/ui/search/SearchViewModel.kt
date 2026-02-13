package com.foodvisor.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodvisor.data.model.Restaurant
import com.foodvisor.data.repository.RestaurantRepository
import com.foodvisor.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: RestaurantRepository
) : BaseViewModel() {

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    fun searchRestaurants(
        query: String? = null,
        cuisine: String? = null,
        etoiles: Int? = null,
        prixMax: Double? = null
    ) {
        viewModelScope.launch {
            setLoading(true)
            clearError()

            // Si aucun filtre, récupérer tous les restaurants
            val result = if (query.isNullOrEmpty() && cuisine == null && etoiles == null && prixMax == null) {
                repository.getAllRestaurants()
            } else {
                repository.searchRestaurants(query, cuisine, etoiles, prixMax)
            }

            result.fold(
                onSuccess = { restaurants ->
                    _restaurants.value = restaurants
                    setLoading(false)
                },
                onFailure = { exception ->
                    setError(exception.message ?: "Erreur de recherche")
                    setLoading(false)
                }
            )
        }
    }

    fun toggleFavorite(restaurant: Restaurant) {
        viewModelScope.launch {
            repository.toggleFavorite(restaurant)
        }
    }
}