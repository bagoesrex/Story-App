package com.bagoesrex.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bagoesrex.storyapp.data.local.database.ListStoryItem
import com.bagoesrex.storyapp.data.repository.StoryRepository

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        _isLoading.value = true

        return storyRepository.getPaginatedStories()
            .cachedIn(viewModelScope)
            .also {
                _isLoading.value = false
            }
    }
}
