package com.zeyadgasser.playground.tasks.data

import com.zeyadgasser.playground.tasks.data.network.TaskDTO
import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

//class TaskDataMapper(private val cryptoHelper: CryptoHelper) {
object TaskDataMapper {
    private val dateTimeComponentFormat: DateTimeFormat<LocalDateTime> = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        dayOfMonth()
        char(' ')
        hour()
        char(':')
        minute()
    }

    fun mapDTOToDomain(taskDTO: TaskDTO): TaskDomain = with(taskDTO) {
        TaskDomain(
            formatDate(creationDate),
            formatDate(dueDate),
//                cryptoHelper.instance.decrypt(encryptedDescription).getOrDefault("error"),
            encryptedDescription,
            encryptedTitle,
//                cryptoHelper.instance.decrypt(encryptedTitle).getOrDefault("error"),
            id,
            image,
            false,
            dependencies.orEmpty()
        )
    }

    fun mapDTOsToDomains(taskDTOs: List<TaskDTO>): List<TaskDomain> = taskDTOs.map { mapDTOToDomain(it) }

    fun mapDomainToDTO(task: TaskDomain): TaskDTO = with(task) {
        TaskDTO(creationDate, dueDate, encryptedDescription, encryptedTitle, id, image, emptyList())
    }

    private fun formatDate(date: String): String =
        Instant.parse(date).toLocalDateTime(TimeZone.currentSystemDefault()).format(dateTimeComponentFormat)
}
