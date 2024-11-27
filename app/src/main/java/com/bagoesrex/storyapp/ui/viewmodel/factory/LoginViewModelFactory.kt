package com.bagoesrex.storyapp.ui.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bagoesrex.storyapp.data.pref.UserPreferences
import com.bagoesrex.storyapp.data.repository.AuthRepository
import com.bagoesrex.storyapp.di.Injection
import com.bagoesrex.storyapp.ui.viewmodel.LoginViewModel

class LoginViewModelFactory(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        fun getInstance(context: Context): LoginViewModelFactory {
            val authRepository = Injection.authRepository(context)
            val userPreferences = UserPreferences(context)
            return LoginViewModelFactory(authRepository, userPreferences)
        }
    }
}
