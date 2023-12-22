package ru.vafin.fiit

import java.time.DayOfWeek

class Lesson(
    var dayOfThisPair: DayOfWeek,
    var nameOfSubject: String,
    var numberOfAud: String,
    var timeOfLesson: TimeOfLesson,
    var nameOfTeacher: String,
    var numeratorAndDenominator: NumAndDen,

    ) : Comparable<Lesson> {
    override fun toString(): String {
        return "\\$dayOfThisPair, $nameOfSubject, $numberOfAud, $timeOfLesson, $nameOfTeacher, ${numeratorAndDenominator.name}\\"
    }

    override fun compareTo(other: Lesson): Int {
        return timeOfLesson.compareTo(other.timeOfLesson)
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

fun getEmptyLessonsList(): MutableList<MutableList<Lesson>> {
    return mutableListOf(
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        mutableListOf<Lesson>(),
    )
}