package com.foodvisor.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "foodvisor_prefs")

class PreferencesManager(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    val userId: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_ID] ?: "" }

    val userEmail: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL] ?: "" }

    val userName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME] ?: "" }

    val userPhotoUrl: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[USER_PHOTO_URL] ?: "" }

    suspend fun saveUserData(
        userId: String,
        email: String,
        name: String,
        photoUrl: String? = null
    ) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
            preferences[USER_ID] = userId
            preferences[USER_EMAIL] = email
            preferences[USER_NAME] = name
            photoUrl?.let { preferences[USER_PHOTO_URL] = it }
        }
    }

    suspend fun updateUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}