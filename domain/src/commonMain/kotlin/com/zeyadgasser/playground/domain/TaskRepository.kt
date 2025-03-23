package com.zeyadgasser.playground.domain

import com.zeyadgasser.playground.architecture.domain.Repository
import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import com.zeyadgasser.playground.domain.model.TaskDomain
import kotlinx.coroutines.flow.Flow

@OpenForMokkery
interface TaskRepository : Repository {

    suspend fun getTasks(): List<TaskDomain>

    suspend fun syncTasks(): Boolean

    fun getTasksOfflineFirst(isAndroid: Boolean): Flow<List<TaskDomain>>

    suspend fun getTask(taskId: String): TaskDomain

    suspend fun insertTask(task: TaskDomain): Boolean
}
