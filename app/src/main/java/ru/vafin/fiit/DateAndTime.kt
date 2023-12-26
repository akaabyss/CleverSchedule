package ru.vafin.fiit

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

fun getDayOfWeekByStringWithName(str: String): DayOfWeek {
    return when (str) {
        DayOfWeek.MONDAY.name -> DayOfWeek.MONDAY
        DayOfWeek.TUESDAY.name -> DayOfWeek.TUESDAY
        DayOfWeek.WEDNESDAY.name -> DayOfWeek.WEDNESDAY
        DayOfWeek.THURSDAY.name -> DayOfWeek.THURSDAY
        DayOfWeek.FRIDAY.name -> DayOfWeek.FRIDAY
        DayOfWeek.SATURDAY.name -> DayOfWeek.SATURDAY
        else -> DayOfWeek.SUNDAY
    }
}

var localDateTime: LocalDateTime = LocalDateTime.now()
val calendar: Calendar = Calendar.getInstance()
val weekOfYear = if (calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
    NumAndDen.Denominator
} else {
    NumAndDen.Numerator
}
fun LocalTime.getTimeString(): String {
    return if (hour < 10) {
        "0$hour"
    } else {
        "$hour"
    } + ":" + if (minute < 10) {
        "0$minute"
    } else {
        "$minute"
    } + ":" + if (second < 10) {
        "0$second"
    } else {
        "$second"
    }
}
val daysOfWeek = DayOfWeek.values()
