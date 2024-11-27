package com.bagoesrex.storyapp.di

import android.content.Context
import com.bagoesrex.storyapp.data.pref.UserPreferences
import com.bagoesrex.storyapp.data.remote.retrofit.ApiConfig
import com.bagoesrex.storyapp.data.repository.AuthRepository
import com.bagoesrex.storyapp.data.repository.StoryRepository

object Injection {

    fun authRepository(context: Context): AuthRepository {
        val pref = UserPreferences(context)
        val token = pref.getToken()
        val apiService = if (token != null) {
            ApiConfig.getApiService(token)
        } else {
            ApiConfig.getApiService("")
        }
        return AuthRepository.getInstance(apiService)
    }

    fun storyRepository(context: Context): StoryRepository {
        val pref = UserPreferences(context)
        val token = pref.getToken()
        val apiService = if (token != null) {
            ApiConfig.getApiService(token)
        } else {
            ApiConfig.getApiService("")
        }
        return StoryRepository.getInstance(apiService)
    }
}
