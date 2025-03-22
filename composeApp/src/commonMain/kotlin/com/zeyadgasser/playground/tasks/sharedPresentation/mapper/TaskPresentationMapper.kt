package com.zeyadgasser.playground.tasks.sharedPresentation.mapper

import com.zeyadgasser.playground.tasks.domain.model.TaskDomain
import com.zeyadgasser.playground.tasks.sharedPresentation.model.TaskPM
import com.zeyadgasser.playground.architecture.utils.OpenForMokkery

@OpenForMokkery
class TaskPresentationMapper() {

    fun mapDomainToPresentation(taskDomain: TaskDomain): TaskPM = with(taskDomain) {
        TaskPM(creationDate, dueDate, encryptedDescription, encryptedTitle, id, image, done, dependencies)
    }

    fun mapDomainToPresentation(taskDomain: List<TaskDomain>): List<TaskPM> =
        taskDomain.map { mapDomainToPresentation(it) }

    fun mapPresentationToDomain(taskPM: TaskPM): TaskDomain = with(taskPM) {
        TaskDomain(creationDate, dueDate, encryptedDescription, encryptedTitle, id, image, done, dependencies)
    }

    fun mapPresentationToDomain(taskPM: List<TaskPM>): List<TaskDomain> =
        taskPM.map { mapPresentationToDomain(it) }
}
