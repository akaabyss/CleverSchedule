package ru.vafin.fiit

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem

import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.vafin.fiit.ui.theme.colorOfAllPairs
import ru.vafin.fiit.ui.theme.colorOfText
import ru.vafin.fiit.ui.theme.colorOfThisPair
import ru.vafin.fiit.ui.theme.mainColor
import java.io.File
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

import androidx.compose.material.*
import androidx.compose.runtime.*

var d = LocalDateTime.now()

object FontSize {
    val bitText = 20.sp
}

val calendar = Calendar.getInstance()
val weekOfYear = if (calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
    NumAndDen.Denominator
} else {
    NumAndDen.Numerator
}


class MainActivity : ComponentActivity() {
    private var lessons = getEmptyLessonsList()
    private var fileBaseName = "dataForUniversityApp.txt"
    private val screen1 = "screen_1"
    private val screen2 = "screen_2"
    private val screen3 = "screen_3"


    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lessons = readData()

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController, startDestination = screen1
            ) {
                composable(screen1) {
                    MainScreen({
                        navController.navigate(screen1)
                    }, {
                        navController.navigate(screen2)
                    }, {
                        navController.navigate(screen3)
                    })
                }
                composable(screen2) {
                    ScreenWithPickData({
                        navController.navigate(screen1)
                    }, {
                        navController.navigate(screen2)
                    }, {
                        navController.navigate(screen3)
                    })
                }
                composable(screen3) {
                    EditScreen({
                        navController.navigate(screen1)
                    }, {
                        navController.navigate(screen2)
                    }, {
                        navController.navigate(screen3)
                    })
                }


            }


        }

    }

    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun EditScreen(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {
        val subjectsMut by remember {
            mutableStateOf(lessons)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(), backgroundColor = mainColor,

                ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Редактировать данные", fontSize = 22.sp)
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f)
            ) {
                for (day in daysOfWeek) {
                    val thisDayLessons = subjectsMut[day.value - 1]
                    itemsIndexed(thisDayLessons) { index, lesson ->
                        if (index == 0) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .background(mainColor),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = day.name, fontSize = 18.sp)
                            }
                        }
                        val les = remember {
                            mutableStateOf(lesson)
                        }
                        PairEditCard(
                            les
                        )
                    }
                }
            }
            BottomBar(
                onClickToScreen1, onClickToScreen2, onClickToScreen3, selected3 = true
            )
        }

    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PairEditCard(pair: MutableState<Lesson>) {
        val context = LocalContext.current
        var isEditing by remember {
            mutableStateOf(false)
        }
        var startTime by remember {
            mutableStateOf(pair.value.timeOfLesson.startTime)
        }
        var endTime by remember {
            mutableStateOf(pair.value.timeOfLesson.endTime)
        }
        var nameOfSubject by remember {
            mutableStateOf(pair.value.nameOfSubject)
        }
        var nameOfTeacher by remember {
            mutableStateOf(pair.value.nameOfTeacher)
        }
        var numberOfAud by remember {
            mutableStateOf(pair.value.numberOfAud)
        }
        var numAndDen by remember {
            mutableStateOf(pair.value.numeratorAndDenominator)
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorOfAllPairs,
            ),
        ) {
            val thisFont1 = 17.sp
            val thisFont2 = 15.sp
            val thisFont3 = 17.sp
            val thisFont4 = 15.sp
            val thisFont5 = 15.sp
            val thisExpandedFont1 = 19.sp
            val thisExpandedFont2 = 17.sp
            val thisExpandedFont3 = 17.sp
            val thisExpandedFont4 = 19.sp
            val thisExpandedFont5 = 17.sp

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                ) {
                    if (!isEditing) {
                        Text(text = nameOfSubject, fontSize = thisFont1)
                        Text(text = nameOfTeacher, fontSize = thisFont2)
                        Text(text = numberOfAud, fontSize = thisFont3)
                        Text(
                            text = "${startTime.toShortString()} - ${endTime.toShortString()}",
                            fontSize = thisFont4
                        )
                        Text(text = getStringWithNameByNumOrDen(numAndDen), fontSize = thisFont5)
                    } else {
                        TextField(
                            value = nameOfSubject,
                            onValueChange = {
                                nameOfSubject = it
                            },
                            textStyle = TextStyle(fontSize = thisExpandedFont1),
                            colors = TextFieldDefaults.textFieldColors(containerColor = colorOfAllPairs),
                        )
                        TextField(
                            value = nameOfTeacher,
                            onValueChange = {
                                nameOfTeacher = it
                            },
                            textStyle = TextStyle(fontSize = thisExpandedFont2),
                            colors = TextFieldDefaults.textFieldColors(containerColor = colorOfAllPairs),
                        )
                        TextField(
                            value = numberOfAud,
                            onValueChange = {
                                numberOfAud = it
                            },
                            textStyle = TextStyle(fontSize = thisExpandedFont3),
                            colors = TextFieldDefaults.textFieldColors(containerColor = colorOfAllPairs),
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text(text = startTime.toShortString(),
                                fontSize = thisExpandedFont4,
                                modifier = Modifier.clickable {
                                    TimePickerDialog(
                                        context, { _, hour, minute ->
//                                            timeOfLesson.startTime.hour.plus(hour - timeOfLesson.startTime.hour)
//                                            timeOfLesson.startTime.minute.plus(minute - timeOfLesson.startTime.minute)
                                            startTime = LocalTime.of(hour, minute)
                                        }, startTime.hour, startTime.minute, true
                                    ).show()
                                })
                            Text(text = " - ", fontSize = thisExpandedFont4)
                            Text(text = endTime.toShortString(),
                                fontSize = thisExpandedFont4,
                                modifier = Modifier.clickable {
                                    TimePickerDialog(
                                        context, { _, hour, minute ->
//                                            timeOfLesson.endTime.hour.plus(hour - timeOfLesson.endTime.hour)
//                                            timeOfLesson.endTime.minute.plus(hour - timeOfLesson.endTime.minute)
                                            endTime = LocalTime.of(hour, minute)
                                        }, endTime.hour, endTime.minute, true
                                    ).show()
                                })
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Button(
                                onClick = { numAndDen = NumAndDen.Numerator },
                                enabled = (numAndDen != NumAndDen.Numerator),
                                modifier = Modifier,
                            ) {
                                Text(text = "Числитель", fontSize = thisExpandedFont5)
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { numAndDen = NumAndDen.Every },
                                enabled = (numAndDen != NumAndDen.Every),
                                modifier = Modifier,
                            ) {
                                Text(text = "Всегда", fontSize = thisExpandedFont5)
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { numAndDen = NumAndDen.Denominator },
                                enabled = (numAndDen != NumAndDen.Denominator),
                                modifier = Modifier,
                            ) {
                                Text(text = "Знаменатель", fontSize = thisExpandedFont5)
                            }
                        }
                    }
                }
                IconButton(onClick = {
                    if (isEditing) {
                        pair.value.nameOfSubject = nameOfSubject
                        pair.value.nameOfTeacher = nameOfTeacher
                        pair.value.numberOfAud = numberOfAud
                        pair.value.timeOfLesson.startTime = startTime
                        pair.value.timeOfLesson.endTime = endTime
                        pair.value.numeratorAndDenominator = numAndDen
                        lessons.sortedLessons()
                        writeDataByMutableMap()
                    }
                    isEditing = !isEditing
                }) {
                    if (!isEditing) {
                        Icon(Icons.Default.Edit, contentDescription = "IconEdit")
                    } else {
                        Icon(Icons.Default.Done, contentDescription = "IconDone")
                    }
                }
            }
        }
    }

    @SuppressLint("MutableCollectionMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScreenWithPickData(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {
        LocalContext.current
        var text by remember { mutableStateOf("") }
        var sub by remember {
            mutableStateOf(lessons)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .weight(10f)
                )
                Button(
                    onClick = {
                        lessons = getSubjectsFromListString(text.split("\n"))
                        sub = lessons
//                    writeText(text)
                        writeDataByMutableMap()

                    }, enabled = text.isNotEmpty()
                ) {
                    Text(text = "update subjects")
                }
                BottomBar(
                    onClickToScreen1, onClickToScreen2, onClickToScreen3, selected2 = true
                )
            }
        }
    }

    @Composable
    fun MainScreen(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {

        var datetime by remember {
            mutableStateOf(
                LocalTime.now()
            )
        }
        LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = mainColor,
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.softpaw),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp) //форма
                    )
                    Text("FIIT61", fontSize = 22.sp)
                    Text(text = datetime.getTimeString(), fontSize = 22.sp)
                    IconButton(onClick = {
                        datetime = LocalTime.now()
                    }) {
                        Icon(Icons.Filled.Autorenew, contentDescription = "Info")
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val thisfont = 20.sp
                    Text(
                        text = if (weekOfYear == NumAndDen.Numerator) {
                            "Числитель"
                        } else {
                            "Знаменатель"
                        }, fontSize = thisfont
                    )
                    Text(text = "|", fontSize = thisfont)
                    Text(text = d.dayOfWeek.name, fontSize = thisfont)
//                    Text(text = "|", fontSize = thisfont)
//                    Text(text = "$numberOfSemester сем", fontSize = thisfont)
                }

                Spacer(modifier = Modifier.height(10.dp))
                val listik: MutableList<Lesson> = lessons[d.dayOfWeek.value - 1]
                if (listik.size == 0) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (i in 1..10) {
                            Text(text = "Выходной?", fontSize = 17.sp)
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
                if (listik.isNotEmpty()) {
                    for (indexOfSubject in 0..listik.lastIndex) {
                        val thisLesson = listik[indexOfSubject]
                        if (thisLesson.timeOfLesson.timeVnutri(datetime)) {
                            thisLesson.GetStringForSchedule(colorOfThisPair)
                        } else {
                            thisLesson.GetStringForSchedule(colorOfAllPairs)
                        }

                    }
                }
            }
            BottomBar(
                onClickToScreen1, onClickToScreen2, onClickToScreen3, selected1 = true
            )
        }

    }

    @Composable
    fun BottomBar(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
        selected1: Boolean = false,
        selected2: Boolean = false,
        selected3: Boolean = false,

        ) {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = mainColor,
            contentColor = Color.Black
        ) {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Icon1") },
                selected = selected1,
                onClick = {
                    onClickToScreen1()
                },
                enabled = !selected1,
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = "Icon2") },
                selected = selected2,
                onClick = {
                    onClickToScreen2()
                },
                enabled = !selected2,
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Default.List, contentDescription = "Icon3") },
                selected = selected3,
                onClick = {
                    onClickToScreen3()
                },
                enabled = !selected3,
            )
        }
    }


    @Composable
    fun Lesson.GetNextLessonForSchedule() {
        Card(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = colorOfThisPair
            )
        ) {
            GetStringForSchedule(colorOfAllPairs)
        }
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
                    IconButton(onClick = {
                        maxInfo = !maxInfo
                    }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Filled.Info, contentDescription = "Info")
                    }

                }
            }
        }
    }


    private fun getNumberOfSemester(): Int {
//    var oneSemRange = listOf(8, 9, 10, 11, 12)
        val twoSemRange = listOf(2, 3, 4, 5, 6, 7, 8)
        var cource = d.year - 2022
        val semester: Int
        if (d.month.value !in twoSemRange) {
            cource += 1
            semester = 1
        } else {
            semester = 2
        }

        return (cource - 1) * 2 + semester
    }

    //    private fun writeText(text: String) {
//        val file = File(this.getExternalFilesDir(null), fileBaseName)
//        file.writeText(text)
//        Log.e("MyLog", "writeText = $text")
//    }

    private fun writeDataByMutableMap() {
        try {
            val file = File(this.getExternalFilesDir(null), fileBaseName)
            file.writeText("")
            Log.e("MyLog", "start = WriteDataByMutableMap")
            for (day in daysOfWeek) {
                val subjectsInThisDay = lessons[day.value - 1]
                if (subjectsInThisDay.isNotEmpty()) {
                    for (subject in subjectsInThisDay) {
                        file.appendText(subject.toFileString() + "\n")
                    }
                }
            }
            Toast.makeText(this, "local database has updated", Toast.LENGTH_LONG).show()
            Log.e("MyLog", "end = WriteDataByMutableMap")
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR WRITING", Toast.LENGTH_LONG).show()
//            Log.e("Artur", e.message ?: "")
        }

    }

    private fun readData(): MutableList<MutableList<Lesson>> {
        try {
            val file = File(this.getExternalFilesDir(null), fileBaseName)
            val listWithPairs = file.readLines()
            Log.e("MyLog", "readData = $listWithPairs")
            return getSubjectsFromListString(listWithPairs)
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR READING LOCAL DATABASE", Toast.LENGTH_LONG).show()
        }
        return getEmptyLessonsList()
    }

}

