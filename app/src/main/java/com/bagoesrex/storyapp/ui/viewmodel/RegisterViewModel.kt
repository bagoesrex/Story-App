package com.bagoesrex.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagoesrex.storyapp.data.remote.response.RegisterResponse
import com.bagoesrex.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.launch
import com.bagoesrex.storyapp.data.Result

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val result = authRepository.register(name, email, password)
                _registerResult.value = result
            } catch (e: Exception) {
                _registerResult.value = Result.Error("An error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
