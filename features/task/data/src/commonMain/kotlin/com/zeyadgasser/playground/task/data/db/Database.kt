package com.zeyadgasser.playground.task.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntity::class, TaskDependencyEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class PlaygroundDatabase : RoomDatabase() {
    abstract fun taskDAO(): TasksDao
}
