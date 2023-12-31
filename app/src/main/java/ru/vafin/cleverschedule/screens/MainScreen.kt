package ru.vafin.cleverschedule.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.DropdownMenu
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.vafin.cleverschedule.BottomBar
import ru.vafin.cleverschedule.GetStringForSchedule
import ru.vafin.cleverschedule.NumAndDen
import ru.vafin.cleverschedule.R
import ru.vafin.cleverschedule.TextHint
import ru.vafin.cleverschedule.daysOfWeek
import ru.vafin.cleverschedule.editScreen
import ru.vafin.cleverschedule.getTimeString
import ru.vafin.cleverschedule.localDateTime
import ru.vafin.cleverschedule.readData
import ru.vafin.cleverschedule.screenWithPickData
import ru.vafin.cleverschedule.ui.theme.colorOfAllPairs
import ru.vafin.cleverschedule.ui.theme.colorOfThisPair
import ru.vafin.cleverschedule.ui.theme.mainColor
import ru.vafin.cleverschedule.weekOfYear
import java.time.LocalTime
import kotlin.random.Random

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun MainScreen(
    navController:NavController
) {

    val context = LocalContext.current
    val lessons = remember {
        mutableStateListOf(*(readData(context).toTypedArray()))
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
    val listik = listOf(
        R.drawable.catsleep1,
        R.drawable.catsleep2,
        R.drawable.catsleep3,
        R.drawable.catsleep4,
        R.drawable.catsleep5,
        R.drawable.catsleep6
    )
    var idImage by remember {
        mutableIntStateOf(listik[Random.nextInt(listik.size)])
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

                    var isExpandedInfoAboutMe by remember {
                        mutableStateOf(false)
                    }
                    Box {
                        IconButton(onClick = { isExpandedInfoAboutMe = true }) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Info about me",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = isExpandedInfoAboutMe,
                            onDismissRequest = { isExpandedInfoAboutMe = false }) {
                            Column(modifier = Modifier.padding(5.dp)) {
                                Text(
                                    text = "@akaabyss",
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                    Text(
                        text = "${localDateTime.dayOfMonth}." + if (localDateTime.month.value < 10) {
                            "0${localDateTime.month.value}"
                        } else {
                            localDateTime.month.value
                        },
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
                            if (isExpanded) {
                                Icon(
                                    imageVector = Icons.Filled.ExpandLess,
                                    contentDescription = "time"
                                )
                            } else {

                                Icon(
                                    imageVector = Icons.Filled.ExpandMore,
                                    contentDescription = "time"
                                )
                            }
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
                    for (indexOfLesson in 0..lessonsOfThisDay.lastIndex) {
                        val thisLesson = lessonsOfThisDay[indexOfLesson]
                        if (thisLesson.timeOfLesson.timeVnutri(time) && thisLesson.dayOfThisPair == localDateTime.dayOfWeek) {
                            thisLesson.GetStringForSchedule(colorOfThisPair)
                        } else {
                            thisLesson.GetStringForSchedule(colorOfAllPairs)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Похоже, спим!", fontSize = 20.sp)
                        Text(
                            text = "Ну или смотрим котиков)))",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "тыкай на котика",
                            fontSize = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = idImage),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    var rand = listik[Random.nextInt(listik.size)]
                                    while (rand == idImage) {
                                        rand = listik[Random.nextInt(listik.size)]
                                    }
                                    idImage = rand
                                }
                        )
                    }
                }
            }
        }

        BottomBar(
            clickToScreenWithPickData = { navController.navigate(screenWithPickData) },
            clickToEditScreen = { navController.navigate(editScreen) },
            selected1 = true
        )
    }

}