package com.zeyadgasser.playground.task.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [TaskEntity::class, TaskDependencyEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class PlaygroundDatabase : RoomDatabase() {
    abstract fun taskDAO(): TasksDao
}

fun getRoomDatabase(builder: RoomDatabase.Builder<PlaygroundDatabase>): PlaygroundDatabase =
    builder
//        .addMigrations(MIGRATIONS)
//        .fallbackToDestructiveMigrationOnDowngrade()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
