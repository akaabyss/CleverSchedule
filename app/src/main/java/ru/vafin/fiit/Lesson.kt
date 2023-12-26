package ru.vafin.fiit

import android.util.Log
import java.time.DayOfWeek

data class Lesson(
    var dayOfThisPair: DayOfWeek,
    var nameOfSubject: String,
    var numberOfAud: String,
    var timeOfLesson: TimeOfLesson,
    var nameOfTeacher: String,
    var numeratorAndDenominator: NumAndDen,
    ) : Comparable<Lesson> {
    override fun toString(): String {
        return "|$dayOfThisPair, $nameOfSubject, $numberOfAud, $timeOfLesson, $nameOfTeacher, ${numeratorAndDenominator.name}|"
    }

    override fun compareTo(other: Lesson): Int {
        if (dayOfThisPair.value - other.dayOfThisPair.value == 0)
            return timeOfLesson.compareTo(other.timeOfLesson)
        return dayOfThisPair.value - other.dayOfThisPair.value
    }

    fun toFileString(): String {
        return "${dayOfThisPair.name}, $nameOfSubject, $numberOfAud, ${timeOfLesson.toFileString()}, " + "$nameOfTeacher, ${
            getStringWithNameByNumOrDen(
                numeratorAndDenominator
            )
        };"
    }
}

fun String.fromStringToLessonObject(): Lesson {
    val listByStrSplit = this.split(", ")
    return Lesson(
        getDayOfWeekByStringWithName(listByStrSplit[0]),
        listByStrSplit[1],
        listByStrSplit[2],
        getTimeOfLessonByStringWithNumberOrStringWith4Times(listByStrSplit[3]),
        listByStrSplit[4],
        getNumOrDenByStringWithName(listByStrSplit[5])
    )
}


fun MutableList<MutableList<Lesson>>.removeLesson(lesson: Lesson) {
    Log.e("removeLesson", "leson to remove = ${lesson.toFileString()}")
    this[lesson.dayOfThisPair.value - 1].forEach {
        Log.e("removeLesson", "leson it = ${it.toFileString()}")
        if (lesson.toFileString() == it.toFileString()) {
            this[lesson.dayOfThisPair.value - 1].remove(it)
            Log.e("removeLesson", "removed = ${it.toFileString()}")
            Log.e("removeLesson", "остальное  = ${this}")
        }
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