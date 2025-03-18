package com.zeyadgasser.playground.tasks.domain

import com.zeyadgasser.playground.architecture.domain.Repository
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskRepository : Repository {

    suspend fun getTasks(): List<TaskDomain>

    suspend fun syncTasks(): Boolean

    fun getTasksOfflineFirst(isAndroid: Boolean): Flow<List<TaskDomain>>

    suspend fun getTask(taskId: String): TaskDomain

    suspend fun insertTask(task: TaskDomain): Boolean
}
