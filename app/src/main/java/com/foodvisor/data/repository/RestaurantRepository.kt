package com.foodvisor.data.repository

import android.util.Log
import com.foodvisor.data.api.RetrofitClient
import com.foodvisor.data.local.AppDatabase
import com.foodvisor.data.model.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestaurantRepository(private val database: AppDatabase) {

    private val apiService = RetrofitClient.apiService
    private val restaurantDao = database.restaurantDao()

    companion object {
        private const val TAG = "RestaurantRepository"
    }

    /**
     * Récupère tous les restaurants (API puis cache local)
     */
    suspend fun getAllRestaurants(forceRefresh: Boolean = false): Result<List<Restaurant>> {
        return withContext(Dispatchers.IO) {
            try {
                if (forceRefresh) {
                    // Forcer le rafraîchissement depuis l'API
                    Log.d(TAG, "Fetching restaurants from API...")
                    val response = apiService.getAllRestaurants()

                    if (response.isSuccessful && response.body() != null) {
                        val restaurants = response.body()!!
                        Log.d(TAG, "API returned ${restaurants.size} restaurants")

                        // Sauvegarder en cache
                        restaurantDao.deleteAll()
                        restaurantDao.insertAll(restaurants)

                        Result.success(restaurants)
                    } else {
                        Log.e(TAG, "API error: ${response.code()}")
                        Result.failure(Exception("Erreur API: ${response.code()}"))
                    }
                } else {
                    // Essayer le cache d'abord
                    val cached = restaurantDao.getAllRestaurants()
                    if (cached.isNotEmpty()) {
                        Log.d(TAG, "Returning ${cached.size} cached restaurants")
                        Result.success(cached)
                    } else {
                        // Si pas de cache, récupérer depuis l'API
                        getAllRestaurants(forceRefresh = true)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching restaurants", e)
                // En cas d'erreur réseau, retourner le cache s'il existe
                val cached = restaurantDao.getAllRestaurants()
                if (cached.isNotEmpty()) {
                    Log.d(TAG, "Network error, returning ${cached.size} cached restaurants")
                    Result.success(cached)
                } else {
                    Result.failure(e)
                }
            }
        }
    }

    /**
     * Récupère un restaurant par son ID
     */
    suspend fun getRestaurantById(id: String): Result<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                // Chercher en cache d'abord
                val cached = restaurantDao.getRestaurantById(id)
                if (cached != null) {
                    Log.d(TAG, "Restaurant $id found in cache")
                    Result.success(cached)
                } else {
                    // Sinon, chercher dans l'API
                    Log.d(TAG, "Fetching restaurant $id from API")
                    val response = apiService.getRestaurantById(id)

                    if (response.isSuccessful && response.body() != null) {
                        val restaurant = response.body()!!
                        restaurantDao.insert(restaurant)
                        Result.success(restaurant)
                    } else {
                        Result.failure(Exception("Restaurant introuvable"))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching restaurant $id", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Recherche des restaurants avec filtres
     */
    suspend fun searchRestaurants(
        query: String? = null,
        cuisine: String? = null,
        etoiles: Int? = null,
        prixMax: Double? = null
    ): Result<List<Restaurant>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Searching: query=$query, cuisine=$cuisine, stars=$etoiles, price=$prixMax")

                // Appel API avec filtres
                val response = apiService.searchRestaurants(cuisine, etoiles, prixMax, null)

                if (response.isSuccessful && response.body() != null) {
                    var restaurants = response.body()!!

                    // Filtrage local par nom si query fournie
                    if (!query.isNullOrEmpty()) {
                        restaurants = restaurants.filter {
                            it.nom.contains(query, ignoreCase = true) ||
                                    it.cuisine.contains(query, ignoreCase = true) ||
                                    it.description.contains(query, ignoreCase = true)
                        }
                    }

                    Log.d(TAG, "Search returned ${restaurants.size} results")
                    Result.success(restaurants)
                } else {
                    Result.failure(Exception("Erreur de recherche: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Search error", e)
                // Fallback: recherche locale
                if (!query.isNullOrEmpty()) {
                    val localResults = restaurantDao.searchRestaurants(query)
                    Result.success(localResults)
                } else {
                    Result.failure(e)
                }
            }
        }
    }

    /**
     * Récupère les restaurants favoris
     */
    suspend fun getFavorites(): Result<List<Restaurant>> {
        return withContext(Dispatchers.IO) {
            try {
                val favorites = restaurantDao.getFavorites()
                Log.d(TAG, "Found ${favorites.size} favorites")
                Result.success(favorites)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching favorites", e)
                Result.failure(e)
            }
        }
    }

    /**
     * Toggle le statut favori d'un restaurant
     */
    suspend fun toggleFavorite(restaurant: Restaurant): Result<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Toggling favorite for ${restaurant.id}")

                // Tenter de mettre à jour via l'API
                try {
                    val response = apiService.toggleFavorite(restaurant.id)
                    if (response.isSuccessful && response.body() != null) {
                        val updated = response.body()!!
                        restaurantDao.updateFavoriteStatus(updated.id, updated.isFavorite)
                        Log.d(TAG, "Favorite toggled via API: ${updated.isFavorite}")
                        return@withContext Result.success(updated)
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "API toggle failed, updating locally", e)
                }

                // Fallback: mise à jour locale
                restaurant.isFavorite = !restaurant.isFavorite
                restaurantDao.update(restaurant)
                Log.d(TAG, "Favorite toggled locally: ${restaurant.isFavorite}")
                Result.success(restaurant)

            } catch (e: Exception) {
                Log.e(TAG, "Error toggling favorite", e)
                Result.failure(e)
            }
        }
    }
}