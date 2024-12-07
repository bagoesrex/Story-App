package com.bagoesrex.storyapp

import com.bagoesrex.storyapp.data.local.database.ListStoryItem
import java.util.UUID
import kotlin.random.Random

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val storyList: MutableList<ListStoryItem> = mutableListOf()
        for (index in 0 until 100) {
            val storyItem = ListStoryItem(
                photoUrl = "https://random-image-pepebigotes.vercel.app/api/random-image?random=$index",
                createdAt = "2024-05-${(index % 31) + 1}T05:50:60Z",
                name = "Name of Story $index",
                description = "Descrption of Story $index",
                lon = Random.nextDouble(10.0, 100.0),
                id = UUID.randomUUID().toString(),
                lat = Random.nextDouble(-100.0, 10.0)
            )
            storyList.add(storyItem)
        }
        return storyList
    }
}