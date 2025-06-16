package com.zeyadgasser.playground

import kotlinx.serialization.Serializable

@Serializable
data object TaskList

@Serializable
data class TaskDetail(val taskId: String)

@Serializable
data object BreathingCoachApp

@Serializable
data object RoutineList

@Serializable
data class RoutineDetail(val routineId: Long)

@Serializable
data class RoutineForm(val routineId: Long?)

@Serializable
data object BadHabitList

@Serializable
data class BadHabitDetail(val routineId: Long)

@Serializable
data class BadHabitForm(val routineId: Long?)
