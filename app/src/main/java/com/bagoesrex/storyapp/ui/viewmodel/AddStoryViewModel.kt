package com.bagoesrex.storyapp.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.bagoesrex.storyapp.data.repository.StoryRepository
import com.bagoesrex.storyapp.data.Result
import com.bagoesrex.storyapp.data.remote.response.StoryUploadResponse

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _uploadResult = MutableLiveData<Result<StoryUploadResponse>>()
    val uploadResult: LiveData<Result<StoryUploadResponse>> = _uploadResult

    private val _currentImageUri = MutableLiveData<Uri?>()

    fun setImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

    fun getImageUri(): Uri? {
        return _currentImageUri.value
    }

    fun uploadStory(
        description: RequestBody,
        photo: MultipartBody.Part,
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = storyRepository.uploadStory(description, photo)
                _uploadResult.value = result
            } catch (e: Exception) {
                _uploadResult.value = Result.Error(e.message ?: "Unknown error occurred")
            } finally {
                _isLoading.value = false
            }
        }

    }


}