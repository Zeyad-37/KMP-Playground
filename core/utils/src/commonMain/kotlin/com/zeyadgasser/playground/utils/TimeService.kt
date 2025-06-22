package com.zeyadgasser.playground.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

object TimeService {

    fun getCurrentDateTime(): LocalDateTime =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    fun getCurrentDate(): LocalDate = getCurrentDateTime().date

    fun getCurrentTime(): LocalTime = getCurrentDateTime().time

    fun getCurrentTimeFormatted(): String = getCurrentTime().format(LocalTime.Format {
        hour()
        char(':')
        minute()
    })

    fun getCurrentDateFormatted(): String = getCurrentDate().format(LocalDate.Format {
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    })

    fun getCurrentDateLabel(): String = getCurrentDate().format(LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
        chars(", ")
        dayOfMonth()
        char('/')
        monthNumber()
        char('/')
        year()
    })
}
