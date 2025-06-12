package com.zeyadgasser.playground.routine.sharedpresentation

data class RoutinePM(
    val id: Long = 0,
    val name: String,
    val type: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val category: String,
    val completed: Boolean = false,
    val remindersEnabled: Boolean = false,
    val image: String? = null,
    val rating: Int? = null,
    val icon: Int? = null,
) {
    companion object {
        val EMPTY_ROUTINE = RoutinePM(0, "", "", "", "", "", "")
    }
}
