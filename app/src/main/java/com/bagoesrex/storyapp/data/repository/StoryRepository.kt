package com.bagoesrex.storyapp.data.repository

import com.bagoesrex.storyapp.data.remote.response.StoryResponse
import com.bagoesrex.storyapp.data.remote.retrofit.ApiService
import retrofit2.HttpException
import java.io.IOException
import com.bagoesrex.storyapp.data.Result
import com.bagoesrex.storyapp.data.remote.response.StoryDetailResponse
import com.bagoesrex.storyapp.data.remote.response.StoryUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoryRepository private constructor(private val apiService: ApiService) {

    suspend fun getAllStories(
        page: Int? = null,
        size: Int? = null,
        location: Int = 0
    ): Result<StoryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getStories(page, size, location)

                if (!response.error!!) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message ?: "Unknown error occurred")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Result.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    suspend fun getStory(id: String): Result<StoryDetailResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getStory(id)

                if (!response.error) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message)
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Result.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    suspend fun uploadStory(
        description: RequestBody,
        photo: MultipartBody.Part,
    ): Result<StoryUploadResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.uploadStory(description, photo)

                if (!response.error) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message)
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            } catch (e: HttpException) {
                Result.Error("HTTP error: ${e.message}")
            } catch (e: Exception) {
                Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}
