package ru.vafin.fiit

import android.util.Log

fun getSubjectsFromListString(
    selectedTextByStrings: List<String>,
): MutableList<Lesson> {
    val result = mutableListOf<Lesson>()
    try {
        Log.e("MyLog", "selectedtext = $selectedTextByStrings")
        for (str in selectedTextByStrings) {
            val obj = str.substring(0, str.lastIndex).fromStringToLessonObject()
            result.add(obj)
        }
        Log.e("MyLog", "getsubjectsFromListString (этим будет subjects) = $result")
    } catch (e: Exception) {
        Log.e("MyLog", "exception = ${e.message.toString()}")
    }
    result.sort()
    Log.e("MyLog", "return result in getSubjectsFromListStringog=  $result")
    return result
}
