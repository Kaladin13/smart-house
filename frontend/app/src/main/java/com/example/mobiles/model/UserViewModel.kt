package com.example.mobiles.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _user = MutableStateFlow<UserModel?>(null)
    private val _houses = MutableStateFlow<GetHousesResponse?>(null)
    val user: StateFlow<UserModel?> = _user
    val houses: StateFlow<GetHousesResponse?> = _houses

    fun setUser(user: UserModel) {
        viewModelScope.launch {
            _user.emit(user)
        }
    }
        fun setHouses(houses: GetHousesResponse) {
            viewModelScope.launch {
                _houses.emit(houses)
            }
        }
}