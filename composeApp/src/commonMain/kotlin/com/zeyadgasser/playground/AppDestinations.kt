package com.zeyadgasser.playground

import kotlinx.serialization.Serializable

@Serializable
data object TaskList

@Serializable
data class TaskDetail(val taskId: String)

@Serializable
data object BreathingCoachApp
