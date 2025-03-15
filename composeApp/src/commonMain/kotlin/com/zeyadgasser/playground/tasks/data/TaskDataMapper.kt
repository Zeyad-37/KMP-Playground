package com.zeyadgasser.playground.tasks.data

import com.zeyadgasser.playground.tasks.data.api.TaskDTO
import com.zeyadgasser.playground.tasks.data.db.TaskDependencyEntity
import com.zeyadgasser.playground.tasks.data.db.TaskEntity
import com.zeyadgasser.playground.tasks.data.db.TaskWithDependencies
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import com.zeyadgasser.taskdata.utility.CryptoHelper
import java.text.SimpleDateFormat
import java.util.Locale

class TaskDataMapper(private val cryptoHelper: CryptoHelper) {

    fun mapDTOsToDomains(taskDTOs: List<TaskDTO>): List<TaskDomain> = taskDTOs.map {
        with(it) {
            TaskDomain(
                formatDate(creationDate),
                formatDate(dueDate),
                cryptoHelper.instance.decrypt(encryptedDescription).getOrDefault("error"),
                cryptoHelper.instance.decrypt(encryptedTitle).getOrDefault("error"),
                id,
                image,
                false,
                dependencies.orEmpty()
            )
        }
    }

    fun mapEntityToDomain(taskWithDependencies: TaskWithDependencies): TaskDomain =
        with(taskWithDependencies.task) {
            TaskDomain(
                creationDate,
                dueDate,
                cryptoHelper.instance.decrypt(encryptedDescription).getOrDefault("error"),
                cryptoHelper.instance.decrypt(encryptedTitle).getOrDefault("error"),
                id,
                image,
                done,
                taskWithDependencies.dependencies.map { it.dependencyId }
            )
        }

    fun mapEntitiesToDomains(taskWithDependencies: List<TaskWithDependencies>): List<TaskDomain> =
        taskWithDependencies.map { mapEntityToDomain(it) }

    fun mapDTOsToEntities(taskDTOs: List<TaskDTO>): List<TaskEntity> = taskDTOs.map {
        with(it) {
            TaskEntity(
                formatDate(creationDate),
                formatDate(dueDate),
                cryptoHelper.instance.decrypt(encryptedDescription).getOrDefault("error"),
                cryptoHelper.instance.decrypt(encryptedTitle).getOrDefault("error"),
                id,
                image,
                false,
            )
        }
    }

    fun mapDomainToEntity(task: TaskDomain): TaskEntity = with(task) {
        TaskEntity(creationDate, dueDate, encryptedDescription, encryptedTitle, id, image, done)
    }

    fun taskDependenciesFromDTO(task: TaskDTO): List<TaskDependencyEntity> = with(task) {
        dependencies?.map { dependencyId -> TaskDependencyEntity(id, dependencyId) } ?: emptyList()
    }

    private fun formatDate(date: String): String = with(Locale.getDefault()) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", this).parse(date)
            ?.let { SimpleDateFormat("yyyy-MM-dd HH:mm", this).format(it) }
            ?: date
    }
}
