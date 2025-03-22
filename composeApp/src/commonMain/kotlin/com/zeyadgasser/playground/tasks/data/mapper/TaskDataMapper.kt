package com.zeyadgasser.playground.tasks.data.mapper

import com.zeyadgasser.playground.tasks.data.network.TaskDTO
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import com.zeyadgasser.playground.utils.CryptoHelper
import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

@OpenForMokkery
class TaskDataMapper(private val cryptoHelper: CryptoHelper) {
    private val dateTimeComponentFormat: DateTimeFormat<LocalDateTime> = LocalDateTime.Format {
        year(); char('-'); monthNumber(); char('-'); dayOfMonth(); char(' '); hour(); char(':'); minute()
    }

    fun mapDTOToDomain(taskDTO: TaskDTO, format: Boolean): TaskDomain = with(taskDTO) {
        TaskDomain(
            if (format) formatDate(creationDate) else creationDate,
            if (format) formatDate(dueDate) else dueDate,
            cryptoHelper.decrypt(encryptedDescription),
            cryptoHelper.decrypt(encryptedTitle),
            id,
            image,
            done == true,
            dependencies.orEmpty(),
        )
    }

    fun mapDTOsToDomains(taskDTOs: List<TaskDTO>): List<TaskDomain> =
        taskDTOs.map { mapDTOToDomain(it, false) }

    fun mapDomainToDTO(task: TaskDomain): TaskDTO = with(task) {
        TaskDTO(creationDate, dueDate, encryptedDescription, encryptedTitle, id, image, dependencies, done)
    }

    private fun formatDate(date: String): String =
        Instant.parse(date).toLocalDateTime(TimeZone.currentSystemDefault()).format(dateTimeComponentFormat)
}
