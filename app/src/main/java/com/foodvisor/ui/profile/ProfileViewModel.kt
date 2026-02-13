package com.foodvisor.ui.profile

import androidx.lifecycle.viewModelScope
import com.foodvisor.data.local.PreferencesManager
import com.foodvisor.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val preferencesManager: PreferencesManager
) : BaseViewModel() {

    private val _userId = MutableStateFlow("")
    val userId: StateFlow<String> = _userId

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _userPhotoUrl = MutableStateFlow("")
    val userPhotoUrl: StateFlow<String> = _userPhotoUrl

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            preferencesManager.userId.collect { _userId.value = it }
        }
        viewModelScope.launch {
            preferencesManager.userName.collect { _userName.value = it }
        }
        viewModelScope.launch {
            preferencesManager.userEmail.collect { _userEmail.value = it }
        }
        viewModelScope.launch {
            preferencesManager.userPhotoUrl.collect { _userPhotoUrl.value = it }
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            preferencesManager.updateUserName(name)
            _userName.value = name
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferencesManager.logout()
        }
    }
}