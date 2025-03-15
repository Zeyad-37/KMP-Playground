package com.zeyadgasser.playground.tasks.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TasksAPI(private val httpClient: HttpClient) {
    suspend fun getTasks(): List<TaskDTO> =
        httpClient.get("https://proton-android-testcloud.europe-west1.firebasedatabase.app/tasks.json").body()
}