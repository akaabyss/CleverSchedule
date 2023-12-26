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
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*

var localDateTime: LocalDateTime = LocalDateTime.now()

object FontSize {
    val bitText = 20.sp
}

val calendar = Calendar.getInstance()
val weekOfYear = if (calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
    NumAndDen.Denominator
} else {
    NumAndDen.Numerator
}
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


class MainActivity : ComponentActivity() {
    private var fileBaseName = "dataForUniversityApp.txt"
    private val screen1 = "screen_1"
    private val screen2 = "screen_2"
    private val screen3 = "screen_3"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast
            .makeText(
                this,
                "Автор: @akasoftpaw",
                Toast.LENGTH_LONG
            )
            .show()

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

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun EditScreen(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {
//        var lessonsMutableState by remember {
//            mutableStateOf(lessons)
//        }
        val lessonsMutableState = remember {
            mutableStateListOf(*(readData().toTypedArray()))
        }
        val editingSome = remember {
            mutableStateOf(0)
        }

        val context = LocalContext.current

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(10f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    itemsIndexed(lessonsMutableState) { index, lesson ->
                        var isEditing by remember {
                            mutableStateOf(false)
                        }
                        var expandedDaysOfWeekList by remember {
                            mutableStateOf(false)
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorOfAllPairs,
                            ),
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1f)
                                ) {
                                    if (!isEditing) {
                                        Text(
                                            text = lesson.nameOfSubject,
                                            fontSize = thisFont1
                                        )
                                        Text(
                                            text = lesson.nameOfTeacher,
                                            fontSize = thisFont2
                                        )
                                        Text(
                                            text = lesson.numberOfAud,
                                            fontSize = thisFont3
                                        )
                                        Text(
                                            text = "${lesson.timeOfLesson.startTime.toShortString()} - ${lesson.timeOfLesson.endTime.toShortString()}",
                                            fontSize = thisFont4
                                        )
                                        Text(
                                            text = "${lesson.dayOfThisPair} : ${
                                                getStringWithNameByNumOrDen(
                                                    lesson.numeratorAndDenominator
                                                )
                                            }",
                                            fontSize = thisFont5
                                        )
                                    } else {
                                        TextField(
                                            value = lessonsMutableState[index].nameOfSubject,
                                            onValueChange = {
                                                lessonsMutableState[index] =
                                                    lessonsMutableState[index].copy(nameOfSubject = it)
                                            },
                                            textStyle = TextStyle(fontSize = thisExpandedFont1),
                                            colors = TextFieldDefaults.textFieldColors(
                                                containerColor = colorOfAllPairs
                                            ),
                                        )
                                        TextField(
                                            value = lessonsMutableState[index].nameOfTeacher,
                                            onValueChange = {
                                                lessonsMutableState[index] =
                                                    lessonsMutableState[index].copy(nameOfTeacher = it)

                                            },
                                            textStyle = TextStyle(fontSize = thisExpandedFont2),
                                            colors = TextFieldDefaults.textFieldColors(
                                                containerColor = colorOfAllPairs
                                            ),
                                        )
                                        TextField(
                                            value = lessonsMutableState[index].numberOfAud,
                                            onValueChange = {
                                                lessonsMutableState[index] =
                                                    lessonsMutableState[index].copy(numberOfAud = it)

                                            },
                                            textStyle = TextStyle(fontSize = thisExpandedFont3),
                                            colors = TextFieldDefaults.textFieldColors(
                                                containerColor = colorOfAllPairs
                                            ),
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceAround,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(5.dp)
                                        ) {
                                            Row {
                                                Text(text = lessonsMutableState[index].timeOfLesson.startTime.toShortString(),
                                                    fontSize = thisExpandedFont4,
                                                    modifier = Modifier.clickable {
                                                        TimePickerDialog(
                                                            context,
                                                            { _, hour, minute ->
                                                                lessonsMutableState[index] =
                                                                    lessonsMutableState[index].copy(
                                                                        timeOfLesson = TimeOfLesson(
                                                                            hour, minute,
                                                                            lessonsMutableState[index].timeOfLesson.endTime.hour,
                                                                            lessonsMutableState[index].timeOfLesson.endTime.minute,
                                                                        )
                                                                    )
                                                            },
                                                            lessonsMutableState[index].timeOfLesson.startTime.hour,
                                                            lessonsMutableState[index].timeOfLesson.startTime.minute,
                                                            true
                                                        ).show()
                                                    })
                                                TextHint(text = "click")
                                            }
                                            Text(text = " - ", fontSize = thisExpandedFont4)
                                            Row {
                                                Text(text = lessonsMutableState[index].timeOfLesson.endTime.toShortString(),
                                                    fontSize = thisExpandedFont4,
                                                    modifier = Modifier.clickable {
                                                        TimePickerDialog(
                                                            context,
                                                            { _, hour, minute ->

                                                                lessonsMutableState[index] =
                                                                    lessonsMutableState[index].copy(
                                                                        timeOfLesson = TimeOfLesson(
                                                                            lessonsMutableState[index].timeOfLesson.startTime.hour,
                                                                            lessonsMutableState[index].timeOfLesson.startTime.minute,
                                                                            hour, minute,
                                                                        )
                                                                    )
                                                            },
                                                            lessonsMutableState[index].timeOfLesson.endTime.hour,
                                                            lessonsMutableState[index].timeOfLesson.endTime.minute,
                                                            true
                                                        ).show()
                                                    })
                                                TextHint(text = "click")
                                            }
                                        }

                                        Box {
                                            Row {
                                                Text(
                                                    text = lessonsMutableState[index].dayOfThisPair.name,
                                                    modifier = Modifier.clickable {
                                                        expandedDaysOfWeekList = true
                                                    },
                                                    fontSize = thisExpandedFont4
                                                )
                                                TextHint("click")
                                            }
                                            DropdownMenu(
                                                expanded = expandedDaysOfWeekList,
                                                onDismissRequest = {
                                                    expandedDaysOfWeekList = false
                                                },
                                                modifier = Modifier.padding(4.dp)
                                            ) {
                                                for (day in daysOfWeek) {
                                                    Row {
                                                        if (day == lessonsMutableState[index].dayOfThisPair) {
                                                            Icon(
                                                                imageVector = Icons.Filled.Done,
                                                                contentDescription = "thisDayOfWeek"
                                                            )
                                                        } else {
                                                            Spacer(modifier = Modifier.width(25.dp))
                                                        }
                                                        Text(
                                                            text = day.name,
                                                            modifier = Modifier.clickable {
                                                                lessonsMutableState[index].dayOfThisPair =
                                                                    day
                                                                expandedDaysOfWeekList = false
                                                            },
                                                            fontSize = thisExpandedFont4
                                                        )
                                                        Spacer(modifier = Modifier.width(25.dp))
                                                    }
                                                }
                                            }
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Button(
                                                onClick = {
                                                    lessonsMutableState[index] =
                                                        lessonsMutableState[index].copy(
                                                            numeratorAndDenominator =
                                                            NumAndDen.Numerator
                                                        )
                                                },
                                                enabled = (lessonsMutableState[index].numeratorAndDenominator != NumAndDen.Numerator),
                                                modifier = Modifier,
                                            ) {
                                                Text(
                                                    text = "Числитель",
                                                    fontSize = thisExpandedFont5
                                                )
                                            }
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Button(
                                                onClick = {
                                                    lessonsMutableState[index] =
                                                        lessonsMutableState[index].copy(
                                                            numeratorAndDenominator =
                                                            NumAndDen.Every
                                                        )
                                                },
                                                enabled = (lessonsMutableState[index].numeratorAndDenominator != NumAndDen.Every),
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
                                                onClick = {
                                                    lessonsMutableState[index] =
                                                        lessonsMutableState[index].copy(
                                                            numeratorAndDenominator =
                                                            NumAndDen.Denominator
                                                        )
                                                },
                                                enabled = (lessonsMutableState[index].numeratorAndDenominator != NumAndDen.Denominator),
                                                modifier = Modifier,
                                            ) {
                                                Text(
                                                    text = "Знаменатель",
                                                    fontSize = thisExpandedFont5
                                                )
                                            }
                                        }
                                    }
                                }
                                Column {
                                    IconButton(onClick = {
                                        if (isEditing) {
                                            writeDataToFile(lessonsMutableState)
                                            editingSome.value -= 1
                                        } else {
                                            editingSome.value += 1
                                        }
                                        isEditing = !isEditing
                                    }) {
                                        if (!isEditing) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = "IconEdit"
                                            )
                                        } else {
                                            Icon(
                                                Icons.Default.Done,
                                                contentDescription = "IconDone"
                                            )
                                        }
                                    }
                                    IconButton(onClick = {
//                        if (lessons[lessonsMutableState[index].dayOfThisPair.value - 1].removeLesson(pair.value))
//                            Toast.makeText(context, pair.toString(), Toast.LENGTH_LONG).show()
                                        lessonsMutableState.remove(lesson)
                                        Log.e("MyLog", "delete = $lesson")
                                        Toast.makeText(context, "deleted?", Toast.LENGTH_SHORT)
                                            .show()
                                        writeDataToFile(lessonsMutableState)
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete lesson"
                                        )
                                    }
                                }
                            }
                        }
//                        PairEditCard(
//                            lesson, editingSome,
//                        ) {
//                            Log.e(
//                                "MyLog",
//                                "lessonsMutState before delete = ${lessonsMutableState.toList()}"
//                            )
//                            Log.e("MyLog", "lessons before delete = $lessons")
//                            lessons.remove(lesson)
//                            lessonsMutableState.remove(lesson)
//                            Log.e("MyLog", "removed = $lesson")
//                            Log.e(
//                                "MyLog",
//                                "lessonsMutState after delete = ${lessonsMutableState.toList()}"
//                            )
//                            Log.e("MyLog", "lessons after delete = $lessons")
////                        les = null
//                            writeDataByMutableMap()
//                        }
                    }
                }
                if (editingSome.value == 0) {
                    IconButton(modifier = Modifier
                        .align(Alignment.BottomCenter),
                        onClick = {

                        }) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Add new pair",
                            modifier = Modifier.size(50.dp)
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
    fun PairEditCard(
        pair: Lesson,
        editingSome: MutableState<Int>,

        removingLesson: () -> Unit,
    ) {

        val context = LocalContext.current


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
        var lessons = remember {
            mutableStateListOf(*(readData().toTypedArray()))
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
                        lessons =
                            mutableStateListOf(*(getSubjectsFromListString(text.split("\n")).toTypedArray()))
                        text = ""
                        writeDataToFile(lessons)

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

    @OptIn(ExperimentalStdlibApi::class)
    @Composable
    fun MainScreen(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {
        val context = LocalContext.current
        val lessons = remember {
            mutableStateListOf(*(readData().toTypedArray()))
        }
        var time by remember {
            mutableStateOf(
                LocalTime.now()
            )
        }
        var isExpanded by remember {
            mutableStateOf(false)
        }
        var dayOfLessons by remember {
            mutableStateOf(localDateTime.dayOfWeek)
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = mainColor,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.softpaw),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(30.dp) //форма
                            )
                            Text(text = "softpaw", fontSize = 12.sp)
                        }

                        Text(
                            "${localDateTime.dayOfMonth}.${localDateTime.month.value}",
                            fontSize = 22.sp
                        )
                    }
                    Text(text = "|", fontSize = 22.sp)

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = time.getTimeString(), fontSize = 22.sp)

                        IconButton(onClick = {
                            time = LocalTime.now()
                            dayOfLessons = localDateTime.dayOfWeek
                        }) {
                            Icon(Icons.Filled.Autorenew, contentDescription = "updating")
                        }
                    }
                }


            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val thisfont = 20.sp
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (weekOfYear == NumAndDen.Numerator) {
                                "Числитель"
                            } else {
                                "Знаменатель"
                            }, fontSize = thisfont
                        )
                    }

                    Text(text = "|", fontSize = thisfont)

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box()
                        {
                            Row(modifier = Modifier
                                .clickable {
                                    isExpanded = true
                                }
                                .padding(vertical = 4.dp)) {
                                Text(
                                    text = dayOfLessons.name,
                                    fontSize = thisfont,
                                )
                                TextHint("click")
                            }
                            DropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = {
                                    isExpanded = false
                                    time = LocalTime.now()
                                },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                for (i in 0..<7) {
                                    Row {
                                        if (dayOfLessons == daysOfWeek[i]) {
                                            Icon(
                                                imageVector = Icons.Filled.Done,
                                                contentDescription = "thisDayOfWeek"
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.width(25.dp))
                                        }
                                        Row {
                                            Text(
                                                text = daysOfWeek[i].name,
                                                fontSize = thisfont,
                                                modifier = Modifier.clickable {
                                                    time = LocalTime.now()
                                                    dayOfLessons = daysOfWeek[i]
                                                    isExpanded = false
                                                })
                                            if (daysOfWeek[i] == localDateTime.dayOfWeek) {
                                                TextHint(text = "today")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

//                    Text(text = "|", fontSize = thisfont)
//                    Text(text = "$numberOfSemester сем", fontSize = thisfont)
                }


                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = if (isExpanded) {
                        Modifier
                            .fillMaxSize()
                            .clickable { isExpanded = false }
                    } else {
                        Modifier
                            .fillMaxSize()
                    }
                ) {
                    val lessonsOfThisDay = lessons.filter { it.dayOfThisPair == dayOfLessons }

                    if (lessonsOfThisDay.isNotEmpty()) {
                        for (indexOfSubject in 0..lessonsOfThisDay.lastIndex) {
                            val thisLesson = lessonsOfThisDay[indexOfSubject]
                            if (thisLesson.timeOfLesson.timeVnutri(time)) {
                                thisLesson.GetStringForSchedule(colorOfThisPair)
                            } else {
                                thisLesson.GetStringForSchedule(colorOfAllPairs)
                            }
                        }
                    } else {
                        for (i in 1..10) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Weekends? Rest after death!", fontSize = 17.sp)
                                Spacer(modifier = Modifier.height(5.dp))
                            }
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
        var cource = localDateTime.year - 2022
        val semester: Int
        if (localDateTime.month.value !in twoSemRange) {
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

    private fun writeDataToFile(lessons: MutableList<Lesson>) {
        try {
            val file = File(this.getExternalFilesDir(null), fileBaseName)
            file.writeText("")
            Log.e("MyLog", "start = WriteDataByMutableMap")
            if (lessons.isNotEmpty()) {
                for (lesson in lessons) {
                    file.appendText(lesson.toFileString() + "\n")
                }
            }
            Toast.makeText(this, "local database has updated", Toast.LENGTH_SHORT).show()
            Log.e("MyLog", "end = WriteDataByMutableMap")
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR WRITING", Toast.LENGTH_LONG).show()
//            Log.e("Artur", e.message ?: "")
        }

    }

    private fun readData(): MutableList<Lesson> {
        try {
            val file = File(this.getExternalFilesDir(null), fileBaseName)
            val listWithPairs = file.readLines()
            Log.e("MyLog", "readData = $listWithPairs")
            return getSubjectsFromListString(listWithPairs)
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR READING LOCAL DATABASE", Toast.LENGTH_SHORT).show()
        }
        return mutableListOf()
    }

}

