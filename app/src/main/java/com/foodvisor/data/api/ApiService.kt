package com.foodvisor.data.api

import com.foodvisor.data.model.Restaurant
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("restaurants")
    suspend fun getAllRestaurants(): Response<List<Restaurant>>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: String): Response<Restaurant>

    @GET("restaurants/search")
    suspend fun searchRestaurants(
        @Query("cuisine") cuisine: String? = null,
        @Query("etoiles") etoiles: Int? = null,
        @Query("prixMax") prixMax: Double? = null,
        @Query("ville") ville: String? = null
    ): Response<List<Restaurant>>

    @GET("restaurants/favorites")
    suspend fun getFavorites(): Response<List<Restaurant>>

    @POST("restaurants/{id}/toggle-favorite")
    suspend fun toggleFavorite(@Path("id") id: String): Response<Restaurant>

    @POST("restaurants")
    suspend fun createRestaurant(@Body restaurant: Restaurant): Response<Restaurant>

    @PUT("restaurants/{id}")
    suspend fun updateRestaurant(
        @Path("id") id: String,
        @Body restaurant: Restaurant
    ): Response<Restaurant>

    @DELETE("restaurants/{id}")
    suspend fun deleteRestaurant(@Path("id") id: String): Response<Unit>

    @GET("health")
    suspend fun checkHealth(): Response<Map<String, Any>>
}