package ru.vafin.fiit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vafin.fiit.ui.theme.colorOfText
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
        return "|${dayOfThisPair.name.toDefaultString()}, $nameOfSubject, $numberOfAud, $timeOfLesson, $nameOfTeacher, ${numeratorAndDenominator.name}|"
    }

    override fun compareTo(other: Lesson): Int {
        if (dayOfThisPair.value - other.dayOfThisPair.value == 0)
            return timeOfLesson.compareTo(other.timeOfLesson)
        return dayOfThisPair.value - other.dayOfThisPair.value
    }

    fun toFileString(): String {
        return "${dayOfThisPair.name.toDefaultString()}, $nameOfSubject, $numberOfAud, ${timeOfLesson.toFileString()}, " + "$nameOfTeacher, ${
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

@Composable
fun Lesson.GetStringForSchedule(colorBack: Color) {
    if (nameOfSubject != "") {
        var maxInfo by remember {
            mutableStateOf(false)
        }
        if (numeratorAndDenominator == weekOfYear || numeratorAndDenominator == NumAndDen.Every) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .background(colorBack),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier
                        .width(65.dp)
                        .weight(1f)
                ) {
                    Text(text = timeOfLesson.getTime(), fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier.weight(4f)
                ) {
                    Text(text = nameOfSubject, color = colorOfText, fontSize = 20.sp)
                    Text(
                        text = numberOfAud, color = colorOfText, fontSize = 17.sp
                    )
                    if (maxInfo) {
                        Text(text = "Препод: $nameOfTeacher", fontSize = 17.sp)
                    }
                }
                if (nameOfTeacher != "") {
                    IconButton(
                        onClick = {
                            maxInfo = !maxInfo
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = "Info")
                    }
                }
            }
        }
    }
}