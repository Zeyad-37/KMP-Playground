package com.zeyadgasser.taskdomain

import com.zeyadgasser.architecture.domain.Repository
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskRepository : Repository {

    fun test(): Any

    suspend fun getTasks(): List<TaskDomain>

    suspend fun syncTasks(): Boolean

    fun getTasksOfflineFirst(): Flow<List<TaskDomain>>

    suspend fun getTask(taskId: String): TaskDomain

    suspend fun insertTask(task: TaskDomain): Boolean
}
