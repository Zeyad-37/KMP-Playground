package com.zeyadgasser.playground.task.data

import com.zeyadgasser.playground.task.domain.model.TaskDomain
import com.zeyadgasser.playground.task.data.network.TaskDTO

object TestingData {
    val taskFormattedDTO = TaskDTO(
        "2019-06-23 18:00",
        "2020-12-17 18:00",
        "encryptedDescription",
        "encryptedTitle",
        "1",
        "image"
    )
    val taskDTO = TaskDTO(
        "2019-06-23T18:00:00.511Z",
        "2020-12-17T18:00:00.511Z",
        "encryptedDescription",
        "encryptedTitle",
        "1",
        "image"
    )
    val taskDTO2 = TaskDTO(
        "2018-06-23T18:00:00.511Z",
        "2020-12-17T18:00:00.511Z",
        "encryptedDescription",
        "encryptedTitle",
        "2",
        "image"
    )
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
}
