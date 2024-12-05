package com.bagoesrex.storyapp.ui.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bagoesrex.storyapp.data.repository.StoryRepository
import com.bagoesrex.storyapp.di.Injection
import com.bagoesrex.storyapp.ui.viewmodel.StoryMapsViewModel

class StoryMapsViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryMapsViewModel::class.java)) {
            return StoryMapsViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: StoryMapsViewModelFactory? = null

        fun getInstance(context: Context): StoryMapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: StoryMapsViewModelFactory(
                    Injection.storyRepository(context),
                )
            }.also { instance = it }
    }
}