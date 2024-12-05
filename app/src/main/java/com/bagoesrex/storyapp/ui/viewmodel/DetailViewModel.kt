package com.bagoesrex.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagoesrex.storyapp.data.remote.response.StoryDetailResponse
import com.bagoesrex.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import com.bagoesrex.storyapp.data.Result

class DetailViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = MutableLiveData<StoryDetailResponse>()
    val story: LiveData<StoryDetailResponse> = _story

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getStory(id: String) {
        viewModelScope.launch {
            try {
                storyRepository.getStory(id).let { result ->
                    when (result) {
                        is Result.Loading -> {
                            _isLoading.value = true
                        }
                        is Result.Success -> {
                            _isLoading.value = false
                            _story.value = result.data
                        }
                        is Result.Error -> {
                            _isLoading.value = false
                            _errorMessage.value = result.error
                        }
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
