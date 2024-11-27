package com.bagoesrex.storyapp.ui.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bagoesrex.storyapp.data.repository.AuthRepository
import com.bagoesrex.storyapp.di.Injection
import com.bagoesrex.storyapp.ui.viewmodel.RegisterViewModel

class RegisterViewModelFactory(private val authRepository: AuthRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        fun getInstance(context: Context): RegisterViewModelFactory {
            val authRepository = Injection.authRepository(context)
            return RegisterViewModelFactory(authRepository)
        }
    }
}
