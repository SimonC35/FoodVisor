package com.foodvisor.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.foodvisor.data.model.Restaurant
import com.foodvisor.data.repository.RestaurantRepository
import com.foodvisor.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: RestaurantRepository
) : BaseViewModel() {

    private val _favorites = MutableLiveData<List<Restaurant>>()
    val favorites: LiveData<List<Restaurant>> = _favorites

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            setLoading(true)
            clearError()

            val result = repository.getFavorites()
            result.fold(
                onSuccess = { favorites ->
                    _favorites.value = favorites
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
                    // Rafraîchir la liste des favoris
                    loadFavorites()
                },
                onFailure = { exception ->
                    setError(exception.message ?: "Erreur de mise à jour")
                }
            )
        }
    }
}