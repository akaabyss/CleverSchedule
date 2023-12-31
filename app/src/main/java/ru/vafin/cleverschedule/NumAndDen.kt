package ru.vafin.cleverschedule

enum class NumAndDen {
    Every, Numerator, Denominator,
}

fun getNumOrDenByStringWithName(str: String): NumAndDen {
    return when (str.toDefaultString()) {
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