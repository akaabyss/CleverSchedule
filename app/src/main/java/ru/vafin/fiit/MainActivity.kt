package ru.vafin.fiit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

var d = LocalDateTime.now()


val calendar = Calendar.getInstance()
val weekOfYear = if (calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
    MainActivity.NumAndDen.Denominator
} else {
    MainActivity.NumAndDen.Numerator
}

fun LocalTime.getTimeString(): String {
    return if (hour < 10) {
        "0$hour"
    } else {
        "$hour"
    } + ":" + if (minute < 10) {
        "0$minute"
    } else {
        "$minute"
    } + ":" + if (second < 10) {
        "0$second"
    } else {
        "$second"
    }

}

class MainActivity : ComponentActivity() {
    private var lessons = getEmptyLessonsList()

    private var fileBaseName = "dataForUniversityApp.txt"

    private val screen1 = "screen_1"
    private val screen2 = "screen_2"
    private val screen3 = "screen_3"

    private val daysOfWeek = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )

    private fun getEmptyLessonsList(): MutableList<MutableList<Lesson>> {
        return mutableListOf<MutableList<Lesson>>(
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
        )
    }

    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lessons = readData()

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = screen1
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
                    val thisDay = subjectsMut[day.value - 1]
                    itemsIndexed(thisDay) { index, _ ->
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
                        PairEditCard(
                            day = day,
                            index = index
                        )
                    }
                }
            }
            BottomBar(
                onClickToScreen1,
                onClickToScreen2,
                onClickToScreen3,
                selected3 = true
            )
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PairEditCard(day: DayOfWeek, index: Int) {
        var isEditing by remember {
            mutableStateOf(false)
        }
        val pair by remember {
            mutableStateOf(lessons[day.value - 1][index])
        }
        val timeOfLesson by remember {
            mutableStateOf(pair.timeOfLesson)
        }
        var textNameOfSubject by remember {
            mutableStateOf(pair.nameOfSubject)
        }
        var textNameOfTeacher by remember {
            mutableStateOf(pair.nameOfTeacher)
        }
        var textNumberOfAud by remember {
            mutableStateOf(pair.numberOfAud)
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
            val thiExpandedFont1 = 19.sp
            val thiExpandedFont2 = 17.sp
            val thiExpandedFont3 = 17.sp
            val thiExpandedFont4 = 19.sp
            val verticalPaddingOfNumbersInDropMenu = 3.dp
            val horizontalPaddingOfNumbersInDropMenu = 5.dp
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                ) {
                    if (!isEditing) {
//                        Text(text = pair.nameOfSubject, fontSize = thisFont1)
                        Text(text = textNameOfSubject, fontSize = thisFont1)
                        Text(text = textNameOfTeacher, fontSize = thisFont2)
                        Text(text = textNumberOfAud, fontSize = thisFont3)
                        Text(text = timeOfLesson.toString(), fontSize = thisFont4)
                    } else {
//                        TextField(
//                            value = pair.nameOfSubject,
//                            onValueChange = { pair.nameOfSubject = it },
//                            textStyle = TextStyle(fontSize = thisFont1)
//                        )
                        TextField(
                            value = textNameOfSubject,
                            onValueChange = {
                                textNameOfSubject = it
                                pair.nameOfSubject = it
                            },
                            textStyle = TextStyle(fontSize = thiExpandedFont1),
                            colors = TextFieldDefaults.textFieldColors(containerColor = colorOfAllPairs),
                        )
                        TextField(
                            value = textNameOfTeacher,
                            onValueChange = {
                                textNameOfTeacher = it
                                pair.nameOfTeacher = it
                            },
                            textStyle = TextStyle(fontSize = thiExpandedFont2),
                            colors = TextFieldDefaults.textFieldColors(containerColor = colorOfAllPairs),
                        )
                        TextField(
                            value = textNumberOfAud,
                            onValueChange = {
                                textNumberOfAud = it
                                pair.numberOfAud = it
                            },
                            textStyle = TextStyle(fontSize = thiExpandedFont3),
                            colors = TextFieldDefaults.textFieldColors(containerColor = colorOfAllPairs),
                        )
                        var selectedHour by remember {
                            mutableIntStateOf(0) // or use  mutableStateOf(0)
                        }

                        var selectedMinute by remember {
                            mutableIntStateOf(0) // or use  mutableStateOf(0)
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val thisFont = thiExpandedFont4
                            Box {
                                var expanded by remember {
                                    mutableStateOf(false)
                                }
                                Text(
                                    text = timeOfLesson.startHour.toString(),
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clickable { expanded = true },
                                    fontSize = thisFont
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(colorOfAllPairs),
                                ) {
                                    for (i in 1..23) {
                                        Text(
                                            text = "$i", modifier = Modifier
                                                .clickable {
                                                    timeOfLesson.startHour = i
                                                    expanded = !expanded
                                                }
                                                .padding(
                                                    horizontal = horizontalPaddingOfNumbersInDropMenu,
                                                    vertical = verticalPaddingOfNumbersInDropMenu
                                                ),
                                            fontSize = thisFont
                                        )
                                    }
                                }
                            }
                            Text(
                                text = ":",
                                fontSize = thisFont
                            )
                            Box {
                                var expanded by remember {
                                    mutableStateOf(false)
                                }
                                Text(
                                    text = if (timeOfLesson.startMinutes < 10) {
                                        "0${timeOfLesson.startMinutes}"
                                    } else {
                                        "${timeOfLesson.startMinutes}"
                                    },
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clickable { expanded = true },
                                    fontSize = thisFont
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(colorOfAllPairs),
                                ) {
                                    for (i in 0..59) {

                                        Text(
                                            text = "$i", modifier = Modifier
                                                .clickable {
                                                    timeOfLesson.startMinutes = i
                                                    expanded = !expanded
                                                }
                                                .padding(
                                                    horizontal = horizontalPaddingOfNumbersInDropMenu,
                                                    vertical = verticalPaddingOfNumbersInDropMenu
                                                ),
                                            fontSize = thisFont
                                        )


                                    }
                                }
                            }
                            Text(
                                text = " - ",
                                fontSize = thisFont
                            )
                            Box {
                                var expanded by remember {
                                    mutableStateOf(false)
                                }
                                Text(
                                    text = timeOfLesson.endHour.toString(),
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clickable { expanded = true },
                                    fontSize = thisFont
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(colorOfAllPairs),
                                ) {
                                    for (i in 0..23) {
                                        Text(
                                            text = "$i", modifier = Modifier
                                                .clickable {
                                                    timeOfLesson.endHour = i
                                                    expanded = !expanded
                                                }
                                                .padding(
                                                    horizontal = horizontalPaddingOfNumbersInDropMenu,
                                                    vertical = verticalPaddingOfNumbersInDropMenu
                                                ),
                                            fontSize = thisFont
                                        )
                                    }
                                }
                            }
                            Text(
                                text = ":",
                                fontSize = thisFont
                            )
                            Box {
                                var expanded by remember {
                                    mutableStateOf(false)
                                }
                                Text(
                                    text = if (timeOfLesson.endMinutes < 10) {
                                        "0${timeOfLesson.endMinutes}"
                                    } else {
                                        "${timeOfLesson.endMinutes}"
                                    },
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clickable { expanded = true },
                                    fontSize = thisFont
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { },
                                    modifier = Modifier.background(colorOfAllPairs),
                                ) {
                                    for (i in 0..59) {
                                        Text(
                                            text = "$i", modifier = Modifier
                                                .clickable {
                                                    timeOfLesson.endMinutes = i
                                                    expanded = !expanded
                                                }
                                                .padding(
                                                    horizontal = horizontalPaddingOfNumbersInDropMenu,
                                                    vertical = verticalPaddingOfNumbersInDropMenu
                                                ),
                                            fontSize = thisFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                IconButton(onClick = {
                    if (isEditing) {
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
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = text, onValueChange = { text = it },
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
                    onClickToScreen1,
                    onClickToScreen2,
                    onClickToScreen3,
                    selected2 = true
                )
            }
//            Column(
//                modifier = Modifier
//                    .fillMaxSize(),
//                verticalArrangement = Arrangement.Bottom
//            ) {
//
//            }


        }

    }


    private fun getDayOfWeekByStringWithName(str: String): DayOfWeek {
        return when (str) {
            DayOfWeek.MONDAY.name -> DayOfWeek.MONDAY
            DayOfWeek.TUESDAY.name -> DayOfWeek.TUESDAY
            DayOfWeek.WEDNESDAY.name -> DayOfWeek.WEDNESDAY
            DayOfWeek.THURSDAY.name -> DayOfWeek.THURSDAY
            DayOfWeek.FRIDAY.name -> DayOfWeek.FRIDAY
            DayOfWeek.SATURDAY.name -> DayOfWeek.SATURDAY
            else -> DayOfWeek.SUNDAY
//            "Monday" -> DayOfWeek.MONDAY
//            "Tuesday" -> DayOfWeek.TUESDAY
//            "Wednesday" -> DayOfWeek.WEDNESDAY
//            "Thursday" -> DayOfWeek.THURSDAY
//            "Friday" -> DayOfWeek.FRIDAY
//            "Saturday" -> DayOfWeek.SATURDAY
//            else -> DayOfWeek.SUNDAY
        }
    }

//    private fun emptyPair(): Lesson {
//        return Lesson(
//            DayOfWeek.Monday, "", "", TimeOfLesson(),
//            "",
//            NumAndDen.Every
//        )
//    }

    private fun getNumOrDenByStringWithName(str: String): NumAndDen {
        return when (str) {
            "Числитель" -> NumAndDen.Numerator
            "Знаменатель" -> NumAndDen.Denominator
            else -> NumAndDen.Every
        }
    }

    private fun getStringWithNameByNumOrDen(numAndDen: NumAndDen): String {
        return when (numAndDen) {
            NumAndDen.Numerator -> "Числитель"
            NumAndDen.Denominator -> "Знаменатель"
            NumAndDen.Every -> "Всегда"
        }
    }

    private fun MutableList<MutableList<Lesson>>.sortedLessons() {
        for (day in daysOfWeek) {
            val lessonsInThisDay = this[day.value - 1]
            if (lessonsInThisDay.size != 0) {
//                subjects?.set(day, lessonsInThisDay?.sorted())
                lessonsInThisDay.sort()
                this[day.value - 1] = lessonsInThisDay
            }
        }
    }

    private fun getSubjectsFromListString(
        selectedTextByStrings: List<String>
    ): MutableList<MutableList<Lesson>> {
        var result = mutableListOf<MutableList<Lesson>>(
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
            mutableListOf<Lesson>(),
        )
        try {


            for (str in selectedTextByStrings) {
                val listByStrSplit = str.substring(0, str.lastIndex).split(", ")
                val thisDay: DayOfWeek = getDayOfWeekByStringWithName(listByStrSplit[0])
                result[thisDay.value - 1].add(
                    Lesson(
                        getDayOfWeekByStringWithName(listByStrSplit[0]),
                        listByStrSplit[1],
                        listByStrSplit[2],
                        listByStrSplit[3].stringToTimeOfPair(),
                        listByStrSplit[4],
                        getNumOrDenByStringWithName(listByStrSplit[5])
                    )
                )


            }
            Log.e("MyLog", "getsubjectsFromListString (этим будет subjects) = $result")
        } catch (e: Exception) {
            Log.e("MyLog", "exception = ${e.message.toString()}")
        }
        result.sortedLessons()
        Log.e("MyLog", "return result {str681}=  $result")
        return result
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
            modifier = Modifier
                .fillMaxSize()
        ) {

            TopAppBar(
                modifier = Modifier.fillMaxWidth(), backgroundColor = mainColor,

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
                        },
                        fontSize = thisfont
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
                var needNext = true
                if (listik.isNotEmpty()) {
                    var nowIsPair = false
                    for (indexOfSubject in 0..listik.lastIndex) {
                        val thisLesson = listik[indexOfSubject]
                        if (thisLesson.timeOfLesson.timeVnutri(datetime)) {
                            thisLesson.GetStringForSchedule(colorOfThisPair)
                            nowIsPair = true
                        } else {
                            thisLesson.GetStringForSchedule(colorOfAllPairs)
                            needNext = true
                        }

                    }
                }
            }
            BottomBar(
                onClickToScreen1,
                onClickToScreen2,
                onClickToScreen3,
                selected1 = true
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

    fun String.fromStringToPairObject(): Lesson {
        val listByStrSplit = this.split(", ")
        return Lesson(
            getDayOfWeekByStringWithName(listByStrSplit[0]),
            listByStrSplit[1],
            listByStrSplit[2],
            getTimeOfPairByStringWithNumberOrStringWith4Times(listByStrSplit[3]),
            listByStrSplit[4],
            getNumOrDenByStringWithName(listByStrSplit[5])
        )
    }

    fun Lesson.toFileString(): String {
        return "${dayOfThisPair.name}, $nameOfSubject, $numberOfAud, ${timeOfLesson.toFileString()}, " +
                "$nameOfTeacher, ${getStringWithNameByNumOrDen(numeratorAndDenominator)};"
    }

    @Composable
    fun Lesson.GetNextLessonForSchedule() {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = colorOfThisPair
            )
        ) {
            GetStringForSchedule(colorOfAllPairs)
        }
    }


    @Composable
    fun Lesson.GetStringForSchedule(colorBack: Color) {
        if (nameOfSubject != "") {
            var maxLines by remember {
                mutableIntStateOf(1)
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
                            .width(55.dp)

                    ) {
                        Text(text = timeOfLesson.getTime(), fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Column(
                        modifier = Modifier,

                        )
                    {
                        Text(text = nameOfSubject, color = colorOfText, fontSize = 20.sp)
                        Text(
                            text = "$numberOfAud\nПрепод: $nameOfTeacher",
                            color = colorOfText,
                            maxLines = maxLines,
                            fontSize = 17.sp
                        )
                    }
                    IconButton(onClick = {
                        maxLines = 4 - maxLines
                    }) {
                        Icon(Icons.Filled.Info, contentDescription = "Info")
                    }

                }
            }
        }
    }


    private fun getTimeOfPairByStringWithNumberOrStringWith4Times(str: String): TimeOfLesson {
        return if (str.length == 1) {
            when (str.toInt()) {
                1 -> TimeOfLesson(8, 0, 9, 35)
                2 -> TimeOfLesson(9, 45, 11, 20)
                3 -> TimeOfLesson(11, 30, 13, 5)
                4 -> TimeOfLesson(13, 25, 15, 0)
                5 -> TimeOfLesson(15, 10, 16, 45)
                6 -> TimeOfLesson(16, 55, 18, 20)
                7 -> TimeOfLesson(18, 30, 20, 5)
                else -> TimeOfLesson(0, 0, 0, 0)
            }
        } else {
            str.stringToTimeOfPair()
        }
    }

    fun String.stringToTimeOfPair(): TimeOfLesson {
        val twotimes = this.split("-")
        val fortimes = listOf(
            twotimes[0].split(":"),
            twotimes[1].split(":"),
        )
        return TimeOfLesson(
            fortimes[0][0].toInt(),
            fortimes[0][1].toInt(),
            fortimes[1][0].toInt(),
            fortimes[1][1].toInt()
        )
    }


    enum class NumAndDen {
        Every,
        Numerator,
        Denominator,
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

//    override fun onDestroy() {
//        super.onDestroy()
//        writeDataByMutableMap()
//
//    }
}

