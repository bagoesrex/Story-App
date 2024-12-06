package com.bagoesrex.storyapp.di

import android.content.Context
import com.bagoesrex.storyapp.data.local.database.StoryDatabase
import com.bagoesrex.storyapp.data.pref.UserPreferences
import com.bagoesrex.storyapp.data.remote.retrofit.ApiConfig
import com.bagoesrex.storyapp.data.repository.AuthRepository
import com.bagoesrex.storyapp.data.repository.StoryRepository

object Injection {

    fun authRepository(context: Context): AuthRepository {
        val pref = UserPreferences(context)
        val apiService = ApiConfig.getApiService(pref)
        return AuthRepository.getInstance(apiService)
    }

    fun storyRepository(context: Context): StoryRepository {
        val pref = UserPreferences(context)
        val apiService = ApiConfig.getApiService(pref)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, database)
    }
}
