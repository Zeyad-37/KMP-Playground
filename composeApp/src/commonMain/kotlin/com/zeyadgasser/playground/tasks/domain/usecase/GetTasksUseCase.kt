package com.zeyadgasser.playground.tasks.domain.usecase

import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import com.zeyadgasser.taskdomain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class GetTasksUseCase(private val taskRepository: TaskRepository) {

    fun invoke(): Flow<List<TaskDomain>> =
        taskRepository.getTasksOfflineFirst()
            .distinctUntilChanged()
            .map { list -> list.sortedBy { it.creationDate } }
}
