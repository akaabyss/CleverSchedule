package ru.vafin.fiit

import java.time.LocalTime

class TimeOfLesson(
    startTime: Int = 0,
    startMinute: Int = 0,
    endTime: Int = 0,
    endMinute: Int = 0,
) : Comparable<TimeOfLesson> {
    var startTime: LocalTime = LocalTime.of(startTime, startMinute)
    var endTime: LocalTime = LocalTime.of(endTime, endMinute)
    override fun compareTo(other: TimeOfLesson): Int {
        if (startTime.hour - other.startTime.hour == 0) {
            if (startTime.minute - other.startTime.minute == 0) {
                return 0
            }
            return startTime.minute - other.startTime.minute
        }
        return startTime.hour - other.startTime.hour
    }

    override fun toString(): String {
        return "${startTime.hour}:" + if (startTime.minute < 10) {
            "0${startTime.minute}"
        } else {
            "${startTime.minute}"
        } + "-${endTime.hour}:" + if (endTime.minute < 10) {
            "0${endTime.minute}"
        } else {
            "${endTime.minute}"
        }
    }


    fun toFileString(): String {
        return "${startTime.hour}:${startTime.minute}-${endTime.hour}:${endTime.minute}"
    }

    fun getTime(): String {

        return if (startTime.hour < 10) {
            " ${startTime.hour}:"
        } else {
            "${startTime.hour}:"
        } + if (startTime.minute < 10) {
            "0${startTime.minute}"
        } else {
            "${startTime.minute}"
        } + "\n   -\n" + if (endTime.hour < 10) {
            " ${endTime.hour}:"
        } else {
            "${endTime.hour}:"
        } + if (endTime.minute < 10) {
            "0${endTime.minute}"
        } else {
            "${endTime.minute}"
        }
    }

    fun timeVnutri(datetime: LocalTime): Boolean {
        if ((datetime.hour * 60 * 60 + datetime.minute * 60 + datetime.second) in
            (startTime.hour * 60 * 60 + startTime.minute * 60)..(endTime.hour * 60 * 60 + endTime.minute * 60)
        ) {
            return true
        }
        return false
    }
}

fun LocalTime.toShortString(): String {
    return if (hour < 10) {
        " ${hour}:"
    } else {
        "${hour}:"
    } + if (minute < 10) {
        "0${minute}"
    } else {
        "$minute"
    }
}

fun String.stringToTimeOfPair(): TimeOfLesson {
    val twoTimes = this.replace(" ", "").split("-")
    val forTimes = listOf(
        twoTimes[0].split(":"),
        twoTimes[1].split(":"),
    )
    return TimeOfLesson(
        forTimes[0][0].toMyInt(),
        forTimes[0][1].toMyInt(),
        forTimes[1][0].toMyInt(),
        forTimes[1][1].toMyInt()
    )
}

fun String.toMyInt(): Int {
    try {
        return this.toInt()
    } catch (e: Exception) {
        return when (this) {
            "01" -> 1
            "02" -> 2
            "03" -> 3
            "04" -> 4
            "05" -> 5
            "06" -> 6
            "07" -> 7
            "08" -> 8
            "09" -> 9
            else -> 0
        }
    }

}

fun getTimeOfLessonByStringWithNumberOrStringWith4Times(str: String): TimeOfLesson {
    return if (str.length == 1) {
        when (str.toInt()) {
            1 -> TimeOfLesson(8, 0, 9, 35)
            2 -> TimeOfLesson(9, 45, 11, 20)
            3 -> TimeOfLesson(11, 30, 13, 5)
            4 -> TimeOfLesson(13, 25, 15, 0)
            5 -> TimeOfLesson(15, 10, 16, 45)
            6 -> TimeOfLesson(16, 55, 18, 20)
            7 -> TimeOfLesson(18, 30, 20, 5)
            else -> TimeOfLesson(0, 0, 0, 0)
        }
    } else {
        str.stringToTimeOfPair()
    }
}

fun MutableList<MutableList<Lesson>>.sortedLessons() {
    for (day in daysOfWeek) {
        val lessonsInThisDay = this[day.value - 1]
        if (lessonsInThisDay.size != 0) {
//                subjects?.set(day, lessonsInThisDay?.sorted())
            lessonsInThisDay.sort()
            this[day.value - 1] = lessonsInThisDay
        }
    }
}