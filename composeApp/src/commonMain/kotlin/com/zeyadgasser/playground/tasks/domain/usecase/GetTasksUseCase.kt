package com.zeyadgasser.playground.tasks.domain.usecase

import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class GetTasksUseCase(private val taskRepository: TaskRepository) {

    suspend fun invoke(): Flow<List<TaskDomain>> {
        taskRepository.getTasks() // todo: work manager on android
        return taskRepository.getTasksOfflineFirst()
            .distinctUntilChanged()
            .map { list -> list.sortedBy { it.creationDate } }
    }
}
