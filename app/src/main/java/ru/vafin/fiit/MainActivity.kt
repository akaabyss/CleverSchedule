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
import java.io.File
import java.util.Calendar

var d = MainActivity.DateTime()

class MainActivity : ComponentActivity() {

    private var fileBaseName = "database.txt"
    private var subjects: MutableMap<DayOfWeek, MutableList<Pair>>? = null

    //        getSubjectsFromText(fileBaseName)
    private val numberOfSemester = getNumberOfSemester()
    private var fileName: String? = null
    private val screen1 = "screen_1"
    private val screen2 = "screen_2"

    private var mainColor = Color(0xFF9FE778)

    //    Color(0xFFA50AE7)
    private var colorOfThisPair = Color(0xFF9FE778)
    private var colorOfAllPair = Color.LightGray
    var colorOfText = Color.Black

    @SuppressLint("MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
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
                subjects = getSubjectsFromText(context, text.split(";\n"))
                sub = subjects
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
            "Tuesday" -> DayOfWeek.Tuesday
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

    fun getSubjectsFromText(
        context: Context,
        selectedTextbyStrings: List<String>
    ): MutableMap<DayOfWeek, MutableList<Pair>>? {

//        return mutableMapOf<DayOfWeek, MutableList<Pair>>(
//
//            DayOfWeek.Monday to mutableListOf(Pair()),
//            DayOfWeek.Tuesday to mutableListOf(
//                Pair(
//                    "Структуры",
//                    "314",
//                    TimeOfPair(8, 0, 8, 45),
//                    "Авсеева", NumAndDen.Every
//                ),
//                Pair(
//                    "Структуры",
//                    "314",
//                    TimeOfPair(8, 0, 9, 45),
//                    "Авсеева", NumAndDen.Every
//                ),
//                Pair(
//                    "Структуры",
//                    "314",
//                    TimeOfPair(8, 0, 10, 45),
//                    "Авсеева", NumAndDen.Every
//                ),
//                Pair(
//                    "Структуры",
//                    "314",
//                    TimeOfPair(8, 0, 11, 45),
//                    "Авсеева", NumAndDen.Every
//                ),
//            ),
//            DayOfWeek.Wednesday to mutableListOf(Pair()),
//            DayOfWeek.Thursday to mutableListOf(Pair()),
//            DayOfWeek.Friday to mutableListOf(Pair()),
//            DayOfWeek.Saturday to mutableListOf(Pair()),
//            DayOfWeek.Sunday to mutableListOf(Pair()),
//
//            )

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
            Log.e("MyLog", "listStr.size = ${listString.size}")
            for (str in listString) {
                val listByStrSplit = str.split(", ")
                Log.e("MyLog", "listbysplit = $listByStrSplit")
                val thisDay: DayOfWeek = getDayOfWeekByStringWithName(listByStrSplit[0])
                Log.e("MyLog", "day = ${thisDay.name}")
                Log.e("MyLog", "num of pair = ${listByStrSplit[3].toInt()}")
                result[thisDay]?.add(
                    Pair(
                        listByStrSplit[1],
                        listByStrSplit[2],
                        getTimeOfPairByNumberOfPair(listByStrSplit[3].toInt()),
                        listByStrSplit[4],
                        getNumOrDenByStringWithName(listByStrSplit[5])
                    )
                )

                Log.e("MyLog", result.toString())

            }
            return result
        } catch (e: Exception) {
            Log.e("MyLog", e.message.toString())

        }
        return null

    }

    @Composable
    fun Screen2(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
    ) {
        val context = LocalContext.current
        Column(Modifier.fillMaxSize()) {
            Button(onClick = {
                try {
//                    applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE)
//                        .use {
//                            it.write("$username, $userbio".toByteArray())
//                        }
//
//                    Toast.makeText(
//                        applicationContext,
//                        "Файл $fileName сохранён",
//                        Toast.LENGTH_SHORT
//                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }, modifier = Modifier.width(200.dp)) {
                Text(text = "update")
            }

            Button(onClick = {
//                textFromFile = File(applicationContext.filesDir, fileName)
//                    .bufferedReader()
//                    .use { it.readText(); }
//                Toast.makeText(context, "OPen", Toast.LENGTH_LONG).show()
//                Toast.makeText(context, textFromFile, Toast.LENGTH_LONG).show()
//                textBool = true

            }, modifier = Modifier.width(200.dp)) {
                Text(text = "getData")
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                BottomBar(
                    onClickToScreen1,
                    onClickToScreen2,
                    selected2 = true
                )
            }
        }
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

    inner class Pair(
        private var nameOfSubject: String = "",
        private var numberOfAud: String = "",
        var timeOfPair: TimeOfPair = TimeOfPair(),
        var nameOfTeacher: String = "",
        var numeratorAndDenominator: NumAndDen = NumAndDen.Every,
    ) {
//        Monday, Стр Дан, 504П, 4, Авсеева О.В, Числитель
        override fun toString(): String {
            return "[$nameOfSubject, $numberOfAud, $timeOfPair, $nameOfTeacher, ${numeratorAndDenominator.name}]"
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

    private fun getTimeOfPairByNumberOfPair(number: Int): TimeOfPair {
        return when (number) {
            1 -> TimeOfPair(8, 0, 9, 35)
            2 -> TimeOfPair(9, 45, 11, 20)
            3 -> TimeOfPair(11, 30, 13, 5)
            4 -> TimeOfPair(13, 25, 15, 0)
            5 -> TimeOfPair(15, 10, 16, 45)
            6 -> TimeOfPair(16, 55, 18, 20)
            7 -> TimeOfPair(18, 30, 20, 5)
            else -> TimeOfPair(0, 0, 0, 0)
        }
    }

    class TimeOfPair(
        var startHour: Int = 0,
        private var startMinutes: Int = 0,
        var endHour: Int = 0,
        var endMinutes: Int = 0
    ) {
        override fun toString(): String {
            return "$startHour:$startMinutes - $endHour:$endMinutes"
        }
        fun toFileString():String{
            return "$startHour|$startMinutes|$endHour|$endMinutes"
        }

        fun getTime(): String {

            return if (startHour < 10) {
                " $startHour:"
            } else {
                "$startHour:"
            } + if (startMinutes < 10) {
                "0$startMinutes"
            } else {
                "$startMinutes"
            } + "\n   -\n" + if (endHour < 10) {
                " $endHour:"
            } else {
                "$endHour:"
            } + if (endMinutes < 10) {
                "0$endMinutes"
            } else {
                "$endMinutes"
            }
        }

        fun timeVnutri(datetime: DateTime): Boolean {
            if ((datetime.hour * 60 * 60 + datetime.minute * 60 + datetime.sec) in
                (startHour * 60 * 60 + startMinutes * 60)..(endHour * 60 * 60 + endMinutes * 60)
            ) {
                return true
            }
            return false
        }
    }

    enum class NumAndDen {
        Every,
        Numerator,
        Denominator,
    }

    class DateTime {
        var calendar: Calendar = Calendar.getInstance()
        var hour = 0
        var minute = 0
        var day = 0
        var month = 0
        var year = 0
        var sec = 0
        var dayOfWeek: DayOfWeek? = null
        var weekOfYear: NumAndDen = NumAndDen.Every

        init {
            init()
        }

        fun init() {
            calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                2 -> DayOfWeek.Monday
                3 -> DayOfWeek.Tuesday
                4 -> DayOfWeek.Wednesday
                5 -> DayOfWeek.Thursday
                6 -> DayOfWeek.Friday
                7 -> DayOfWeek.Saturday
                else -> DayOfWeek.Sunday
            }
            minute = calendar.get(Calendar.MINUTE)
            sec = calendar.get(Calendar.SECOND)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH) + 1 // добавляем 1, так как он начинается с 0
            year = calendar.get(Calendar.YEAR)
            weekOfYear = if (calendar.get(Calendar.WEEK_OF_YEAR) % 2 == 0) {
                NumAndDen.Denominator
            } else {
                NumAndDen.Numerator
            }
        }

        fun getTimeString(): String {
            calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
            sec = calendar.get(Calendar.SECOND)
            return if (hour < 10) {
                "0$hour:"
            } else {
                "$hour:"
            } + if (minute < 10) {
                "0$minute:"
            } else {
                "$minute"
            } + if (sec < 10) {
                ":0$sec"
            } else {
                ":$sec"
            }

        }

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


    fun writeData() {
        try {
            val file = File(this.getExternalFilesDir(null), "artur.txt")
//            file.appendText("arturHello!")
            file.writeText("Artur!")
            Toast.makeText(this, "wrote to artur.txt", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR WRITING", Toast.LENGTH_LONG).show()
//            Log.e("Artur", e.message ?: "")
        }

    }


    fun readData(fileBaseNameArg: String = fileBaseName): List<String>? {
        try {
            val file = File(this.getExternalFilesDir(null), fileBaseNameArg)
            val x = file.readLines()
            Toast.makeText(this, "read data", Toast.LENGTH_LONG).show()
            return x
        } catch (e: Exception) {
            Toast.makeText(this, "ERROR WRITING", Toast.LENGTH_LONG).show()

        }
        return null
    }
}

