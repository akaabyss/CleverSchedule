package ru.vafin.fiit

enum class NumAndDen {
    Every, Numerator, Denominator,
}

fun getNumOrDenByStringWithName(str: String): NumAndDen {
    return when (str) {
        "Числитель" -> NumAndDen.Numerator
        "Знаменатель" -> NumAndDen.Denominator
        else -> NumAndDen.Every
    }
}
fun getStringWithNameByNumOrDen(numAndDen: NumAndDen): String {
    return when (numAndDen) {
        NumAndDen.Numerator -> "Числитель"
        NumAndDen.Denominator -> "Знаменатель"
        NumAndDen.Every -> "Всегда"
    }
}