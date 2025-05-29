package com.zeyadgasser.playground.task.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "task_dependencies",
    primaryKeys = ["taskId", "dependencyId"],
    indices = [Index("dependencyId")],
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["dependencyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskDependencyEntity(val taskId: String, val dependencyId: String)
