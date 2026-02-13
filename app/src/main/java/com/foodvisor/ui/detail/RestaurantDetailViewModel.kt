package com.foodvisor.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodvisor.data.model.Restaurant
import com.foodvisor.data.repository.RestaurantRepository
import com.foodvisor.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val repository: RestaurantRepository
) : BaseViewModel() {

    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant

    fun loadRestaurant(id: String) {
        viewModelScope.launch {
            setLoading(true)
            clearError()

            val result = repository.getRestaurantById(id)
            result.fold(
                onSuccess = { restaurant ->
                    _restaurant.value = restaurant
                    setLoading(false)
                },
                onFailure = { exception ->
                    setError(exception.message ?: "Restaurant introuvable")
                    setLoading(false)
                }
            )
        }
    }

    fun toggleFavorite() {
        val currentRestaurant = _restaurant.value ?: return

        viewModelScope.launch {
            val result = repository.toggleFavorite(currentRestaurant)
            result.fold(
                onSuccess = { updatedRestaurant ->
                    _restaurant.value = updatedRestaurant
                },
                onFailure = { exception ->
                    setError(exception.message ?: "Erreur de mise à jour")
                }
            )
        }
    }
}