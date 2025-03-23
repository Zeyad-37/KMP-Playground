package com.zeyadgasser.playground.domain.usecase

import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import com.zeyadgasser.playground.domain.TaskRepository
import com.zeyadgasser.playground.domain.model.Operation
import com.zeyadgasser.playground.domain.model.TaskDomain
import com.zeyadgasser.playground.domain.model.Value

@OpenForMokkery
class CheckTaskUseCase(private val taskRepository: TaskRepository) {

    suspend fun invoke(task: TaskDomain): Pair<Operation, Value> {
        val dependencies = task.dependencies.firstOrNull()?.toIntOrNull() ?: 0
        return if (task.done) {
            Operation(true) to Value(taskRepository.insertTask(task.copy(done = false)))
        } else if (dependencies == 0 && !task.done) {
            Operation(true) to Value(taskRepository.insertTask(task.copy(done = true)))
        } else {
            Operation(false) to Value(task.done)
        }
    }
}
