package ru.vafin.fiit

import java.time.LocalTime


//SWITCH TO LOCALTIME.OF, LOCALTIME.OF
//class TimeOfLessonOLD(
//    var startHour: Int = 0,
//    var startMinutes: Int = 0,
//    var endHour: Int = 0,
//    var endMinutes: Int = 0
//) : Comparable<TimeOfLesson> {
//    override fun compareTo(other: TimeOfLesson): Int {
//        if (startHour - other.startHour == 0) {
//            if (startMinutes - other.startMinutes == 0) {
//                return 0
//            }
//            return startMinutes - other.startMinutes
//        }
//        return startHour - other.startHour
//    }
//
//    override fun toString(): String {
//        return "$startHour:" + if (startMinutes < 10) {
//            "0$startMinutes"
//        } else {
//            "$startMinutes"
//        } + "-$endHour:" + if (endMinutes < 10) {
//            "0$endMinutes"
//        } else {
//            "$endMinutes"
//        }
//    }
//
//
//    fun toFileString(): String {
//        return "$startHour:$startMinutes-$endHour:$endMinutes"
//    }
//
//    fun getTime(): String {
//
//        return if (startHour < 10) {
//            " $startHour:"
//        } else {
//            "$startHour:"
//        } + if (startMinutes < 10) {
//            "0$startMinutes"
//        } else {
//            "$startMinutes"
//        } + "\n   -\n" + if (endHour < 10) {
//            " $endHour:"
//        } else {
//            "$endHour:"
//        } + if (endMinutes < 10) {
//            "0$endMinutes"
//        } else {
//            "$endMinutes"
//        }
//    }
//
//    fun timeVnutri(datetime: LocalTime): Boolean {
//        if ((datetime.hour * 60 * 60 + datetime.minute * 60 + datetime.second) in
//            (startHour * 60 * 60 + startMinutes * 60)..(endHour * 60 * 60 + endMinutes * 60)
//        ) {
//            return true
//        }
//        return false
//    }
//}

class TimeOfLesson(
    startTime: Int = 0, 
    startMinute: Int = 0,
    endTime: Int = 0,
    endMinute: Int = 0
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
        } + "-endTime.hour:" + if (endTime.minute < 10) {
            "0${endTime.minute}"
        } else {
            "${endTime.minute}"
        }
    }


    fun toFileString(): String {
        return "${startTime.minute}:${startTime.minute}-${endTime.hour}:${endTime.minute}s"
    }

    fun getTime(): String {

        return if (startTime.hour < 10) {
            " ${startTime.minute}:"
        } else {
            "${startTime.minute}:"
        } + if (startTime.minute < 10) {
            "0${startTime.minute}"
        } else {
            "${startTime.minute}"
        } + "\n   -\n" + if (endTime.hour < 10) {
            " ${endTime.hour}:"
        } else {
            "${endTime.hour}:"
        } + if (endTime.minute < 10) {
            "0${endTime.minute}s"
        } else {
            "${endTime.minute}s"
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
