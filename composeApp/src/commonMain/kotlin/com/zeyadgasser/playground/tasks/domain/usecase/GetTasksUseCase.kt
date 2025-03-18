package com.zeyadgasser.playground.tasks.domain.usecase

import com.zeyadgasser.playground.tasks.domain.TaskRepository
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class GetTasksUseCase(private val taskRepository: TaskRepository) {

    fun invoke(): Flow<List<TaskDomain>> = taskRepository.getTasksOfflineFirst()
        .onEach { delay(1_000) }
        .distinctUntilChanged()
        .map { list -> list.sortedBy { it.creationDate } }
}
