package com.bagoesrex.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagoesrex.storyapp.data.remote.response.ListStoryItem
import com.bagoesrex.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import com.bagoesrex.storyapp.data.Result

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    val storyList: LiveData<List<ListStoryItem>> = _storyList

    fun getStories() {
        _isLoading.value = true

        viewModelScope.launch {
            when (val result = storyRepository.getAllStories()) {
                is Result.Success -> {
                    _storyList.value = result.data.listStory
                }
                is Result.Error -> {
                    _storyList.value = emptyList()
                }
                Result.Loading -> _isLoading.value = true
            }
            _isLoading.value = false
        }
    }
}
