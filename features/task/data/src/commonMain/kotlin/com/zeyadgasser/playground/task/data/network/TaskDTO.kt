package com.zeyadgasser.playground.task.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDTO(
    @SerialName("creation_date")
    val creationDate: String,
    @SerialName("due_date")
    val dueDate: String,
    @SerialName("encrypted_description")
    val encryptedDescription: String,
    @SerialName("encrypted_title")
    val encryptedTitle: String,
    @SerialName("id")
    val id: String,
    @SerialName("image")
    val image: String,
    @SerialName("dependencies")
    val dependencies: List<String>? = null,
    val done: Boolean? = null,
)
