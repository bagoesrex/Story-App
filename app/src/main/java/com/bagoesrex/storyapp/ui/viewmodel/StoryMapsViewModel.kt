package com.bagoesrex.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagoesrex.storyapp.data.remote.response.ListStoryItem
import com.bagoesrex.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import com.bagoesrex.storyapp.data.Result

class StoryMapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<Result<List<ListStoryItem>>>()
    val stories: LiveData<Result<List<ListStoryItem>>> = _stories

    fun getAllStoriesWithMap(location: Int = 1) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val result = storyRepository.getAllStoriesWithMap(location)
                _stories.value = when (result) {
                    is Result.Success -> Result.Success(result.data.listStory)
                    is Result.Error -> Result.Error("Failed to fetch stories")
                    Result.Loading -> null
                }
            } catch (e: Exception) {
                _stories.value = Result.Error("An error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}