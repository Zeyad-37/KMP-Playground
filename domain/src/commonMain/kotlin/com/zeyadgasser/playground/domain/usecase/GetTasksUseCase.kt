package com.zeyadgasser.playground.domain.usecase

import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import com.zeyadgasser.playground.domain.TaskRepository
import com.zeyadgasser.playground.domain.model.TaskDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OpenForMokkery
class GetTasksUseCase(private val taskRepository: TaskRepository, private val isAndroid: Boolean) {

    fun invoke(): Flow<List<TaskDomain>> = taskRepository.getTasksOfflineFirst(isAndroid)
        .distinctUntilChanged()
        .map { list -> list.sortedBy { it.creationDate } }
}
