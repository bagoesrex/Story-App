package com.bagoesrex.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagoesrex.storyapp.data.pref.UserPreferences
import com.bagoesrex.storyapp.data.remote.response.LoginResponse
import com.bagoesrex.storyapp.data.repository.AuthRepository
import com.bagoesrex.storyapp.data.Result
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val result = authRepository.login(email, password)
                _loginResult.value = result

                if (result is Result.Success) {
                    result.data.loginResult?.token?.let { token ->
                        userPreferences.saveToken(token)
                    }
                }
            } catch (e: Exception) {
                _loginResult.value = Result.Error("An error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
