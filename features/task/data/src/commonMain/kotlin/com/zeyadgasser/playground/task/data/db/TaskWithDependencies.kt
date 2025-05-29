package com.zeyadgasser.playground.task.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithDependencies(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val dependencies: List<TaskDependencyEntity>,
)
