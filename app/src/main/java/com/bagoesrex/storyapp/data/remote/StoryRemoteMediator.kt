package com.bagoesrex.storyapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bagoesrex.storyapp.data.local.database.StoryDatabase
import com.bagoesrex.storyapp.data.remote.response.ListStoryItem
import com.bagoesrex.storyapp.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService,
) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {

        val page = INITIAL_PAGE_INDEX

        try {
            val response =
                apiService.getStories(page, state.config.pageSize)

            val endOfPaginationReached = response.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                database.storyDao().insertStory(response.listStory.map {
                    ListStoryItem(
                        photoUrl = it.photoUrl,
                        createdAt = it.createdAt,
                        name = it.name,
                        description = it.description,
                        lon = it.lon,
                        id = it.id,
                        lat = it.lat
                    )
                })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }
}