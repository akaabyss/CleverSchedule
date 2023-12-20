package ru.vafin.fiit

import android.annotation.SuppressLint
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

var d = DateTime()


class MainActivity : ComponentActivity() {
    private var subjects: MutableMap<DayOfWeek, MutableList<Pair>>? = null
    private var fileBaseName = "dataForUniversityApp.txt"

    private val screen1 = "screen_1"
    private val screen2 = "screen_2"
    private val screen3 = "screen_3"
    private val daysOfWeek = listOf(
        DayOfWeek.Monday,
        DayOfWeek.Tuesday,
        DayOfWeek.Wednesday,
        DayOfWeek.Thursday,
        DayOfWeek.Friday,
        DayOfWeek.Saturday,
        DayOfWeek.Sunday
    )

    //    Color(0xFFA50AE7)


    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        subjects = readData()
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
                    ScreenWithFilePick({
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
            mutableStateOf(subjects)
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
                    val thisDay = subjectsMut?.get(day)
                    if (thisDay != null) {
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
            mutableStateOf((subjects?.get(day))?.get(index) ?: emptyPair())
        }
        val timeOfPair by remember {
            mutableStateOf(pair.timeOfPair)
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
                        Text(text = timeOfPair.toString(), fontSize = thisFont4)
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
                                    text = timeOfPair.startHour.toString(),
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
                                                    timeOfPair.startHour = i
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
                                    text = if (timeOfPair.startMinutes < 10) {
                                        "0${timeOfPair.startMinutes}"
                                    } else {
                                        "${timeOfPair.startMinutes}"
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
                                                    timeOfPair.startMinutes = i
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
                                    text = timeOfPair.endHour.toString(),
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
                                                    timeOfPair.endHour = i
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
                                    text = if (timeOfPair.endMinutes < 10) {
                                        "0${timeOfPair.endMinutes}"
                                    } else {
                                        "${timeOfPair.endMinutes}"
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
                                                    timeOfPair.endMinutes = i
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
    fun ScreenWithFilePick(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {
        LocalContext.current
        var text by remember { mutableStateOf("") }
        var sub by remember {
            mutableStateOf(subjects)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp),
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
                        .height(450.dp)
                )
                Button(onClick = {
                    subjects = getSubjectsFromListString(text.split("\n"))
                    sub = subjects
//                    writeText(text)
                    writeDataByMutableMap()

                }) {
                    Text(text = "update")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                BottomBar(
                    onClickToScreen1,
                    onClickToScreen2,
                    onClickToScreen3,
                    selected2 = true
                )
            }


        }

    }


    private fun getDayOfWeekByStringWithName(str: String): DayOfWeek {
        return when (str) {
            DayOfWeek.Monday.name -> DayOfWeek.Monday
            DayOfWeek.Tuesday.name -> DayOfWeek.Tuesday
            DayOfWeek.Wednesday.name -> DayOfWeek.Wednesday
            DayOfWeek.Thursday.name -> DayOfWeek.Thursday
            DayOfWeek.Friday.name -> DayOfWeek.Friday
            DayOfWeek.Saturday.name -> DayOfWeek.Saturday
            else -> DayOfWeek.Sunday
        }
    }

    private fun emptyPair(): Pair {
        return Pair(
            DayOfWeek.Monday, "", "", TimeOfPair(),
            "",
            NumAndDen.Every
        )
    }

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

    private fun getSubjectsFromListString(
        selectedTextbyStrings: List<String>
    ): MutableMap<DayOfWeek, MutableList<Pair>>? {
        try {

            val result = mutableMapOf<DayOfWeek, MutableList<Pair>>(

                DayOfWeek.Monday to mutableListOf(),
                DayOfWeek.Tuesday to mutableListOf(),
                DayOfWeek.Wednesday to mutableListOf(),
                DayOfWeek.Thursday to mutableListOf(),
                DayOfWeek.Friday to mutableListOf(),
                DayOfWeek.Saturday to mutableListOf(),
                DayOfWeek.Sunday to mutableListOf(),

                )
            for (str in selectedTextbyStrings) {
                val listByStrSplit = str.substring(0, str.lastIndex).split(", ")
                val thisDay: DayOfWeek = getDayOfWeekByStringWithName(listByStrSplit[0])
                result[thisDay]?.add(
                    Pair(
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
            return result
        } catch (e: Exception) {
            Log.e("MyLog", "exception = ${e.message.toString()}")

        }
        return null

    }


    @Composable
    fun MainScreen(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {

        var datetime by remember {
            mutableStateOf(
                DateTime()
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
                        modifier = Modifier.clip(CircleShape).size(30.dp) //форма
                    )
                    Text("FIIT61", fontSize = 22.sp)
                    Text(text = datetime.getTimeString(), fontSize = 22.sp)
                    IconButton(onClick = {
                        datetime = DateTime()
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
                        text = if (d.weekOfYear == NumAndDen.Numerator) {
                            "Числитель"
                        } else {
                            "Знаменатель"
                        },
                        fontSize = thisfont
                    )
                    Text(text = "|", fontSize = thisfont)
                    Text(text = "${datetime.dayOfWeek?.name}", fontSize = thisfont)
//                    Text(text = "|", fontSize = thisfont)
//                    Text(text = "$numberOfSemester сем", fontSize = thisfont)
                }

                Spacer(modifier = Modifier.height(10.dp))
                val listik: MutableList<Pair>? = subjects?.get(datetime.dayOfWeek)
                if (listik?.size == 0) {
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
                if (listik != null) {
                    for (subject in listik) {
                        if (subject.timeOfPair.timeVnutri(datetime)) {
                            subject.GetStringForSchedule(colorOfThisPair)
                        } else {
                            subject.GetStringForSchedule(colorOfAllPairs)
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

    fun String.fromStringToPairObject(): Pair {
        val listByStrSplit = this.split(", ")
        return Pair(
            getDayOfWeekByStringWithName(listByStrSplit[0]),
            listByStrSplit[1],
            listByStrSplit[2],
            getTimeOfPairByStringWithNumberOrStringWith4Times(listByStrSplit[3]),
            listByStrSplit[4],
            getNumOrDenByStringWithName(listByStrSplit[5])
        )
    }

    inner class Pair(
        var dayOfThisPair: DayOfWeek,
        var nameOfSubject: String,
        var numberOfAud: String,
        var timeOfPair: TimeOfPair,
        var nameOfTeacher: String,
        var numeratorAndDenominator: NumAndDen,

        ) {
        //        Monday, Стр Дан, 504П, 4, Авсеева О.В, Числитель
        override fun toString(): String {
            return "[$dayOfThisPair, $nameOfSubject, $numberOfAud, $timeOfPair, $nameOfTeacher, ${numeratorAndDenominator.name}]"
        }

        fun toFileString(): String {
            return "${dayOfThisPair.name}, $nameOfSubject, $numberOfAud, ${timeOfPair.toFileString()}, " +
                    "$nameOfTeacher, ${getStringWithNameByNumOrDen(numeratorAndDenominator)};"
        }


        @Composable
        fun GetStringForSchedule(colorBack: Color) {
            if (nameOfSubject != "") {
                var maxLines by remember {
                    mutableIntStateOf(1)
                }
                if (numeratorAndDenominator == d.weekOfYear || numeratorAndDenominator == NumAndDen.Every) {
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
                            Text(text = timeOfPair.getTime(), fontSize = 15.sp)
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
    }


    enum class DayOfWeek {
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday
    }

    private fun getTimeOfPairByStringWithNumberOrStringWith4Times(str: String): TimeOfPair {
        return if (str.length == 1) {
            when (str.toInt()) {
                1 -> TimeOfPair(8, 0, 9, 35)
                2 -> TimeOfPair(9, 45, 11, 20)
                3 -> TimeOfPair(11, 30, 13, 5)
                4 -> TimeOfPair(13, 25, 15, 0)
                5 -> TimeOfPair(15, 10, 16, 45)
                6 -> TimeOfPair(16, 55, 18, 20)
                7 -> TimeOfPair(18, 30, 20, 5)
                else -> TimeOfPair(0, 0, 0, 0)
            }
        } else {
            str.stringToTimeOfPair()
        }
    }

    fun String.stringToTimeOfPair(): TimeOfPair {
        val twotimes = this.split("-")
        val fortimes = listOf(
            twotimes[0].split(":"),
            twotimes[1].split(":"),
        )
        return TimeOfPair(
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
        if (d.month !in twoSemRange) {
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
            val daysOfWeek = listOf(
                DayOfWeek.Monday,
                DayOfWeek.Tuesday,
                DayOfWeek.Wednesday,
                DayOfWeek.Thursday,
                DayOfWeek.Friday,
                DayOfWeek.Saturday
            )
            for (day in daysOfWeek) {
                val subjectsInThisDay = subjects?.get(day)
                if (subjectsInThisDay != null) {
                    for (subject in subjectsInThisDay) {
                        file.appendText(subject.toFileString() + "\n")
                    }
                }
            }
            Toast.makeText(this, "local database has updated", Toast.LENGTH_LONG).show()
            Log.e("MyLog", "WriteDataByMutableMap")
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR WRITING", Toast.LENGTH_LONG).show()
//            Log.e("Artur", e.message ?: "")
        }

    }

    private fun readData(): MutableMap<DayOfWeek, MutableList<Pair>>? {
        try {
            val file = File(this.getExternalFilesDir(null), fileBaseName)
            val listWithPairs = file.readLines()
            Log.e("MyLog", "readData = $listWithPairs")

            return getSubjectsFromListString(listWithPairs)

        } catch (e: Exception) {
            Toast.makeText(this, "ERROR READING LOCAL DATABASE", Toast.LENGTH_LONG).show()
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        writeDataByMutableMap()

    }
}

