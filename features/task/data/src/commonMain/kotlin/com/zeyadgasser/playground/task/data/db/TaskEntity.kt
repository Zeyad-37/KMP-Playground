package com.zeyadgasser.playground.task.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@Entity(tableName = "Tasks")
@ObjCName("TaskEntity")
data class TaskEntity(
    val creationDate: String,
    val dueDate: String,
    val encryptedDescription: String,
    val encryptedTitle: String,
    @PrimaryKey val id: String,
    val image: String,
//    val dependencies: List<String>? = null,
    val done: Boolean? = null,
)
