package ru.vafin.fiit

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.io.File

private var fileBaseName = "dataForUniversityApp.txt"
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
fun readData(context: Context): MutableList<Lesson> {
    try {
        val file = File(context.getExternalFilesDir(null), fileBaseName)
        val listWithPairs = file.readLines()
        Log.e("MyLog", "readData = $listWithPairs")
        return getSubjectsFromListString(listWithPairs)
    } catch (e: Exception) {
//        Toast.makeText(context, "ERROR READING LOCAL DATABASE", Toast.LENGTH_SHORT).show()
    }
    return mutableListOf()
}
fun MutableList<Lesson>.writeDataToFile(context: Context) {
    try {
        val file = File(context.getExternalFilesDir(null), fileBaseName)
        file.writeText("")
        Log.e("MyLog", "start = WriteDataByMutableMap")
        if (this.isNotEmpty()) {
            for (lesson in this) {
                file.appendText(lesson.toFileString() + "\n")
            }
        }
//            Toast.makeText(this, "local database has updated", Toast.LENGTH_SHORT).show()
        Log.e("MyLog", "end = WriteDataByMutableMap")
    } catch (e: Exception) {
        Toast.makeText(context, "ERROR WRITING", Toast.LENGTH_LONG).show()
//            Log.e("Artur", e.message ?: "")
    }

}