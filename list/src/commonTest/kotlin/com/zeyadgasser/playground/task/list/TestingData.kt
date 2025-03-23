package com.zeyadgasser.playground.task.list

import com.zeyadgasser.playground.task.domain.model.TaskDomain
import com.zeyadgasser.playground.task.sharedpresentation.model.TaskPM

object TestingData {
    val taskDomain = TaskDomain(
        "2019-06-23 18:00",
        "2020-12-17 18:00",
        "encryptedDescription",
        "encryptedTitle",
        "1",
        "image",
        false,
        listOf("1")
    )
    val taskDomain2 = TaskDomain(
        "2018-06-23 18:00",
        "2020-12-17 18:00",
        "encryptedDescription",
        "encryptedTitle",
        "2",
        "image",
        false,
        listOf("2")
    )
    val taskPM = TaskPM(
        "2019-06-23 18:00",
        "2020-12-17 18:00",
        "encryptedDescription",
        "encryptedTitle",
        "1",
        "image",
        false,
        listOf("1")
    )
    val taskPM2 = TaskPM(
        "2018-06-23 18:00",
        "2020-12-17 18:00",
        "encryptedDescription",
        "encryptedTitle",
        "2",
        "image",
        false,
        emptyList()
    )
}
