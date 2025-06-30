package com.zeyadgasser.playground

import kotlinx.serialization.Serializable

@Serializable
data object TaskList

@Serializable
data class TaskDetail(val taskId: String)

@Serializable
data object BreathingCoachApp

@Serializable
data class RoutineDetail(val routineId: Long)

@Serializable
data class RoutineForm(val routineId: Long?)

@Serializable
data class BadHabitDetail(val badHabitId: Long)

@Serializable
data class BadHabitForm(val badHabitId: Long?)
