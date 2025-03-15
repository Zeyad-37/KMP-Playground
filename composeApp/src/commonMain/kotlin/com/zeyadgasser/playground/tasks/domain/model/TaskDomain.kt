package com.zeyadgasser.playground.tasks.domain.model

data class TaskDomain(
    val creationDate: String,
    val dueDate: String,
    val encryptedDescription: String,
    val encryptedTitle: String,
    val id: String,
    val image: String,
    val done: Boolean,
    val dependencies: List<String>,
)
