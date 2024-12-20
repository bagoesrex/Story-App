package com.bagoesrex.storyapp.ui.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bagoesrex.storyapp.data.repository.StoryRepository
import com.bagoesrex.storyapp.di.Injection
import com.bagoesrex.storyapp.ui.viewmodel.DetailViewModel

class DetailViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null
        fun getInstance(context: Context): DetailViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: DetailViewModelFactory(
                    Injection.storyRepository(context)
                ).also { instance = it }
            }
        }
    }
}