package com.bagoesrex.storyapp.data.repository

import com.bagoesrex.storyapp.data.remote.response.LoginResponse
import com.bagoesrex.storyapp.data.remote.response.RegisterResponse
import com.bagoesrex.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import com.bagoesrex.storyapp.data.Result

class AuthRepository private constructor(private val apiService: ApiService) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return makeApiCall { apiService.login(email, password) }
    }

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return makeApiCall { apiService.register(name, email, password) }
    }

    private suspend fun <T> makeApiCall(apiCall: suspend () -> T): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response is LoginResponse && response.error == false || response is RegisterResponse && !response.error) {
                    Result.Success(response)
                } else {
                    val message = when (response) {
                        is LoginResponse -> response.message
                        is RegisterResponse -> response.message
                        else -> "Unknown error occurred"
                    }
                    Result.Error(message ?: "Unknown error occurred")
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
        private var instance: AuthRepository? = null
        fun getInstance(apiService: ApiService): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}
