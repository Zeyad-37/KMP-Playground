package com.zeyadgasser.playground.task.sharedpresentation.mapper

import com.zeyadgasser.playground.architecture.utils.OpenForMokkery
import com.zeyadgasser.playground.domain.model.TaskDomain
import com.zeyadgasser.playground.task.sharedpresentation.model.TaskPM

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
