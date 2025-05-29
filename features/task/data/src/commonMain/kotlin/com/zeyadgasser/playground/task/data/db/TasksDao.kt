package com.zeyadgasser.playground.task.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Transaction
    @Query("SELECT * FROM Tasks ORDER BY creationDate ASC")
    suspend fun getAllTasksWithDependencies(): List<TaskWithDependencies>

    @Transaction
    @Query("SELECT * FROM Tasks ORDER BY creationDate ASC")
    fun getAllTasksWithDependenciesOfflineFirst(): Flow<List<TaskWithDependencies>>

    @Transaction
    @Query("SELECT * FROM Tasks WHERE id = :taskId")
    suspend fun getTaskWithDependency(taskId: String): TaskWithDependencies

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReplace(item: List<TaskEntity>): Unit

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTaskDependency(taskDependencyEntities: List<TaskDependencyEntity>): Unit

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllIgnore(item: List<TaskEntity>): Unit
}
