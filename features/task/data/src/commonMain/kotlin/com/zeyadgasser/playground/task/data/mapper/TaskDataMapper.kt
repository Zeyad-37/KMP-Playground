package com.zeyadgasser.playground.task.data.mapper

import com.zeyadgasser.playground.task.data.db.TaskDependencyEntity
import com.zeyadgasser.playground.task.data.db.TaskEntity
import com.zeyadgasser.playground.task.data.db.TaskWithDependencies
import com.zeyadgasser.playground.task.data.network.TaskDTO
import com.zeyadgasser.playground.task.data.utils.CryptoHelper
import com.zeyadgasser.playground.task.domain.model.TaskDomain
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

class TaskDataMapper(private val cryptoHelper: CryptoHelper) {

    fun mapDTOsToDomains(taskDTOs: List<TaskDTO>): List<TaskDomain> = taskDTOs.map {
        with(it) {
            TaskDomain(
                formatDate(creationDate),
                formatDate(dueDate),
                cryptoHelper.decrypt(encryptedDescription),
                cryptoHelper.decrypt(encryptedTitle),
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
                cryptoHelper.decrypt(encryptedDescription),
                cryptoHelper.decrypt(encryptedTitle),
                id,
                image,
                done == true,
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
                cryptoHelper.decrypt(encryptedDescription),
                cryptoHelper.decrypt(encryptedTitle),
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

    private val dateTimeComponentFormat: DateTimeFormat<LocalDateTime> = LocalDateTime.Format {
        year(); char('-'); monthNumber(); char('-'); dayOfMonth(); char(' '); hour(); char(':'); minute()
    }

    private fun formatDate(date: String): String =
        Instant.parse(date).toLocalDateTime(TimeZone.currentSystemDefault()).format(dateTimeComponentFormat)

}
