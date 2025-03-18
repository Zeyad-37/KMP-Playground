package com.zeyadgasser.playground.tasks.domain.usecase

import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import com.zeyadgasser.playground.tasks.domain.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class GetTasksUseCase(private val taskRepository: TaskRepository) {

    fun invoke(): Flow<List<TaskDomain>> {
         // todo: work manager on android
        return taskRepository.getTasksOfflineFirst()
            .onStart { taskRepository.getTasks() }
            .distinctUntilChanged()
            .map { list -> list.sortedBy { it.creationDate } }
    }
}
