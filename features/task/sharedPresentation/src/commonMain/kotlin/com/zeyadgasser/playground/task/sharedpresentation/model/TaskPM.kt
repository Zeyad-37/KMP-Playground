package com.zeyadgasser.playground.task.sharedpresentation.model

data class TaskPM(
    val creationDate: String,
    val dueDate: String,
    val encryptedDescription: String,
    val encryptedTitle: String,
    val id: String,
    val image: String,
    val done: Boolean,
    val dependencies: List<String>,
)
