package ru.vafin.fiit

class TimeOfPair(
    var startHour: Int = 0,
    var startMinutes: Int = 0,
    var endHour: Int = 0,
    var endMinutes: Int = 0
) {
    override fun toString(): String {
        return "$startHour:$startMinutes - $endHour:$endMinutes"
    }

    fun toFileString(): String {
        return "$startHour:$startMinutes-$endHour:$endMinutes"
    }

    fun getTime(): String {

        return if (startHour < 10) {
            " $startHour:"
        } else {
            "$startHour:"
        } + if (startMinutes < 10) {
            "0$startMinutes"
        } else {
            "$startMinutes"
        } + "\n   -\n" + if (endHour < 10) {
            " $endHour:"
        } else {
            "$endHour:"
        } + if (endMinutes < 10) {
            "0$endMinutes"
        } else {
            "$endMinutes"
        }
    }

    fun timeVnutri(datetime: DateTime): Boolean {
        if ((datetime.hour * 60 * 60 + datetime.minute * 60 + datetime.sec) in
            (startHour * 60 * 60 + startMinutes * 60)..(endHour * 60 * 60 + endMinutes * 60)
        ) {
            return true
        }
        return false
    }
}
