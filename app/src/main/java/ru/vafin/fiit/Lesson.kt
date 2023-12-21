package ru.vafin.fiit

import java.time.DayOfWeek

class Lesson(
    var dayOfThisPair: DayOfWeek,
    var nameOfSubject: String,
    var numberOfAud: String,
    var timeOfLesson: TimeOfLesson,
    var nameOfTeacher: String,
    var numeratorAndDenominator: MainActivity.NumAndDen,

    ) : Comparable<Lesson> {
    override fun toString(): String {
        return "\\$dayOfThisPair, $nameOfSubject, $numberOfAud, $timeOfLesson, $nameOfTeacher, ${numeratorAndDenominator.name}\\"
    }

    override fun compareTo(other: Lesson): Int {
        return timeOfLesson.compareTo(other.timeOfLesson)
    }

}
