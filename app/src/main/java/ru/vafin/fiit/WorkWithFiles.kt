package ru.vafin.fiit

import android.util.Log

fun getSubjectsFromListString(
    selectedTextByStrings: List<String>,
): MutableList<MutableList<Lesson>> {
    val result = getEmptyLessonsList()
    try {
        for (str in selectedTextByStrings) {
            val obj = str.substring(0, str.lastIndex).fromStringToLessonObject()
            result[obj.dayOfThisPair.value - 1].add(obj)
        }
        Log.e("MyLog", "getsubjectsFromListString (этим будет subjects) = $result")
    } catch (e: Exception) {
        Log.e("MyLog", "exception = ${e.message.toString()}")
    }
    result.sortedLessons()
    Log.e("MyLog", "return result {str681}=  $result")
    return result
}
