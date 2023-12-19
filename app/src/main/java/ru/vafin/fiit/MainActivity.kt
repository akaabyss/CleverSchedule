package ru.vafin.fiit

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.vafin.fiit.ui.theme.colorOfAllPair
import ru.vafin.fiit.ui.theme.colorOfText
import ru.vafin.fiit.ui.theme.colorOfThisPair
import ru.vafin.fiit.ui.theme.mainColor
import java.io.File
import java.util.Calendar

var d = DateTime()


class MainActivity : ComponentActivity() {
    var subjects: MutableMap<DayOfWeek, MutableList<Pair>>? = null
    private var fileBaseName = "dataForUniversityApp.txt"

    //        getSubjectsFromText(fileBaseName)
    private val numberOfSemester = getNumberOfSemester()

    private val screen1 = "screen_1"
    private val screen2 = "screen_2"


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
                    })
                }
                composable(screen2) {
                    ScreenWithFilePick({
                        navController.navigate(screen1)
                    }, {
                        navController.navigate(screen2)
                    })
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
    ) {
        val context = LocalContext.current
        var text by remember { mutableStateOf("") }
        var sub by remember {
            mutableStateOf(subjects)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = text, onValueChange = { text = it }, modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            )
            Button(onClick = {
                subjects = getSubjectsFromListString(text.split("\n"))
                sub = subjects
                writeText(text)
//                writeDataByMutableMap()
                Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()
            }) {
                Text(text = "update")
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
            )
            {
                BottomBar(
                    onClickToScreen1,
                    onClickToScreen2,
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

    fun getSubjectsFromListString(
        selectedTextbyStrings: List<String>
    ): MutableMap<DayOfWeek, MutableList<Pair>>? {
        try {
//        val listString = readData(selectedText)
            var listString = selectedTextbyStrings

            var result = mutableMapOf<DayOfWeek, MutableList<Pair>>(

                DayOfWeek.Monday to mutableListOf(),
                DayOfWeek.Tuesday to mutableListOf(),
                DayOfWeek.Wednesday to mutableListOf(),
                DayOfWeek.Thursday to mutableListOf(),
                DayOfWeek.Friday to mutableListOf(),
                DayOfWeek.Saturday to mutableListOf(),
                DayOfWeek.Sunday to mutableListOf(),

                )
            for (str in listString) {
                val listByStrSplit = str.substring(0, str.lastIndex).split(", ")

                val thisDay: DayOfWeek = getDayOfWeekByStringWithName(listByStrSplit[0])
                result[thisDay]?.add(
                    Pair(
                        getDayOfWeekByStringWithName(listByStrSplit[0]),
                        listByStrSplit[1],
                        listByStrSplit[2],
                        getTimeOfPairByStringWithNumberOrStringWith4Times(listByStrSplit[3]),
                        listByStrSplit[4],
                        getNumOrDenByStringWithName(listByStrSplit[5])
                    )
                )


            }
            Log.e("myLog", "getsubjectsFromListString = $result")
            return result
        } catch (e: Exception) {
            Log.e("MyLog", e.message.toString())

        }
        return null

    }


    @Composable
    fun MainScreen(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
    ) {

        var datetime by remember {
            mutableStateOf(
                DateTime()
            )
        }
        val context = LocalContext.current

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

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val thisfont = 20.sp
                    Text(
                        text = if (d.weekOfYear == MainActivity.NumAndDen.Numerator) {
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
                val listik: MutableList<MainActivity.Pair>? = subjects?.get(datetime.dayOfWeek)
                if (listik != null) {
                    for (subject in listik) {
                        if (subject.timeOfPair.timeVnutri(datetime)) {
                            subject.GetStringForSchedule(colorOfThisPair)
                        } else {
                            subject.GetStringForSchedule(colorOfAllPair)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                BottomBar(
                    onClickToScreen1,
                    onClickToScreen2,
                    selected1 = true
                )
            }
        }

    }

    @Composable
    fun BottomBar(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        selected1: Boolean = false,
        selected2: Boolean = false,

        ) {
        BottomNavigation(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ) {
            BottomNavigationItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                selected = selected1,
                onClick = {
                    onClickToScreen1()
                },
                enabled = !selected1,
            )

            BottomNavigationItem(
                icon = { Icon(Icons.Default.Settings, contentDescription = "Profile") },
                selected = selected2,
                onClick = {
                    onClickToScreen2()
                },
                enabled = !selected2,
            )
        }
    }

    fun String.fromStringToPairObject(): Pair {
        var listByStrSplit = this.split(", ")
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
            return "${dayOfThisPair.name}, $nameOfSubject, $numberOfAud,${timeOfPair.toFileString()}, " +
                    "$nameOfTeacher, ${getStringWithNameByNumOrDen(numeratorAndDenominator)}"
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
        if (str.length == 1) {
            return when (str.toInt()) {
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
            return str.stringToTimeOfPair()
        }
    }

    fun String.stringToTimeOfPair(): TimeOfPair {
        val twotimes = this.split("-")
        val fortimes = listOf(
            twotimes[0].split(";"),
            twotimes[1].split(";"),
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

    //    private var subjects: MutableMap<DayOfWeek, MutableList<Pair>>? = null
    fun subjectsToOneList() {
        if (subjects != null) {
            val daysOfWeek = listOf(
                DayOfWeek.Monday,
                DayOfWeek.Tuesday,
                DayOfWeek.Wednesday,
                DayOfWeek.Thursday,
                DayOfWeek.Friday,
                DayOfWeek.Saturday,
                DayOfWeek.Sunday
            )
        }
    }

    private fun writeText(text: String) {
        val file = File(this.getExternalFilesDir(null), fileBaseName)
        file.writeText(text)
        Log.e("myLog", "writeText = $text")
    }

    fun writeDataByMutableMap() {
        try {
            val file = File(this.getExternalFilesDir(null), fileBaseName)
            file.writeText("")
            val daysOfWeek = listOf<DayOfWeek>(
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
                        file.appendText(subject.toFileString() + ";\n")
                    }
                }
            }
            Toast.makeText(this, "wrote to $fileBaseName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR WRITING", Toast.LENGTH_LONG).show()
//            Log.e("Artur", e.message ?: "")
        }

    }

    fun readData(): MutableMap<DayOfWeek, MutableList<Pair>>? {
        try {
            val file = File(this.getExternalFilesDir(null), fileBaseName)
            val listWithPairs = file.readLines()
            Log.e("myLog", "readData = ${listWithPairs}")

            Toast.makeText(this, "read data", Toast.LENGTH_LONG).show()

            Log.e("myLog", "readData = ${subjects.toString()}")
            return getSubjectsFromListString(listWithPairs)

        } catch (e: Exception) {
            Toast.makeText(this, "ERROR WRITING", Toast.LENGTH_LONG).show()
        }
        return null
    }
}

