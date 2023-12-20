package ru.vafin.fiit

import java.util.Calendar

class DateTime {
    var calendar: Calendar = Calendar.getInstance()
    var hour = 0
    var minute = 0
    var day = 0
    var month = 0
    var year = 0
    var sec = 0
    var dayOfWeek: MainActivity.DayOfWeek? = null
    var weekOfYear: MainActivity.NumAndDen = MainActivity.NumAndDen.Every

    init {
        init()
    }

    fun init() {
        calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            2 -> MainActivity.DayOfWeek.Monday
            3 -> MainActivity.DayOfWeek.Tuesday
            4 -> MainActivity.DayOfWeek.Wednesday
            5 -> MainActivity.DayOfWeek.Thursday
            6 -> MainActivity.DayOfWeek.Friday
            7 -> MainActivity.DayOfWeek.Saturday
            else -> MainActivity.DayOfWeek.Sunday
        }
        minute = calendar.get(Calendar.MINUTE)
        sec = calendar.get(Calendar.SECOND)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH) + 1 // добавляем 1, так как он начинается с 0
        year = calendar.get(Calendar.YEAR)
        weekOfYear = if (calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
            MainActivity.NumAndDen.Denominator
        } else {
            MainActivity.NumAndDen.Numerator
        }
    }

    fun getTimeString(): String {
        calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        sec = calendar.get(Calendar.SECOND)
        return if (hour < 10) {
            "0$hour"
        } else {
            "$hour"
        } + ":" + if (minute < 10) {
            "0$minute"
        } else {
            "$minute"
        } + ":" + if (sec < 10) {
            "0$sec"
        } else {
            "$sec"
        }

    }

}
