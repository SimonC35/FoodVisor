package com.foodvisor

import android.app.Application
import com.foodvisor.data.local.AppDatabase
import com.foodvisor.data.local.PreferencesManager
import com.foodvisor.data.repository.RestaurantRepository

class FoodvisorApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val preferencesManager: PreferencesManager by lazy { PreferencesManager(this) }
    val restaurantRepository: RestaurantRepository by lazy { RestaurantRepository(database) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: FoodvisorApplication
            private set
    }
}