package ru.vafin.fiit.screens

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vafin.fiit.BottomBar
import ru.vafin.fiit.FontSize
import ru.vafin.fiit.Lesson
import ru.vafin.fiit.NumAndDen
import ru.vafin.fiit.TimeOfLesson
import ru.vafin.fiit.daysOfWeek
import ru.vafin.fiit.getStringWithNameByNumOrDen
import ru.vafin.fiit.readData
import ru.vafin.fiit.toShortString
import ru.vafin.fiit.ui.theme.colorOfAllPairs
import ru.vafin.fiit.ui.theme.mainColor
import ru.vafin.fiit.writeDataToFile
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun EditScreen(
    clickToMainScreen: () -> Unit,
    clickToScreenWithPickData: () -> Unit,
) {
    val context = LocalContext.current
    val lessonsMutableState = remember {
        mutableStateListOf(*(readData(context).toTypedArray()))
    }
    val editingSome = remember {
        mutableIntStateOf(0)
    }
    var adding by remember {
        mutableStateOf(false)
    }
    var permissionForRemoving by remember {
        mutableStateOf(false)
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
                IconButton(onClick = {
                    permissionForRemoving = !permissionForRemoving
                }) {
                    if (permissionForRemoving) {
                        Icon(imageVector = Icons.Filled.Done,
                            contentDescription = "remove permission for remove",
                            modifier = Modifier.clickable {
                                permissionForRemoving = false
                            })
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "take permission for remove",
                            modifier = Modifier.clickable {
                                permissionForRemoving = true
                            }
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(10f)
        ) {
            if (!adding) {
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
                                if (!isEditing && permissionForRemoving) {
                                    IconButton(onClick = {
                                        lessonsMutableState.remove(lesson)
                                        Log.e("MyLog", "delete = $lesson")
//                                            Toast.makeText(context, "deleted?", Toast.LENGTH_SHORT)
//                                                .show()
                                        lessonsMutableState.writeDataToFile(context)
                                    }) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Delete lesson"
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .weight(1f)
                                ) {
                                    if (!isEditing) {
                                        Text(
                                            text = lesson.nameOfSubject,
                                            fontSize = FontSize.medium
                                        )
                                        if (lesson.nameOfTeacher != "") {
                                            Text(
                                                text = lesson.nameOfTeacher,
                                                fontSize = FontSize.small
                                            )
                                        }
                                        if (lesson.numberOfAud != "") {
                                            Text(
                                                text = lesson.numberOfAud,
                                                fontSize = FontSize.medium
                                            )
                                        }
                                        Text(
                                            text = "${lesson.timeOfLesson.startTime.toShortString()} - ${lesson.timeOfLesson.endTime.toShortString()}",
                                            fontSize = FontSize.small
                                        )
                                        Text(
                                            text = "${lesson.dayOfThisPair} : ${
                                                getStringWithNameByNumOrDen(
                                                    lesson.numeratorAndDenominator
                                                )
                                            }",
                                            fontSize = FontSize.small
                                        )
                                    } else {
                                        TextField(
                                            value = lessonsMutableState[index].nameOfSubject,
                                            onValueChange = {
                                                lessonsMutableState[index] =
                                                    lessonsMutableState[index].copy(
                                                        nameOfSubject = it
                                                    )
                                            },
                                            textStyle = TextStyle(fontSize = FontSize.big),
                                            colors = TextFieldDefaults.textFieldColors(
                                                containerColor = colorOfAllPairs
                                            ),
                                            label = { Text(text = "name of lesson") }
                                        )
                                        TextField(
                                            value = lessonsMutableState[index].nameOfTeacher,
                                            onValueChange = {
                                                lessonsMutableState[index] =
                                                    lessonsMutableState[index].copy(
                                                        nameOfTeacher = it
                                                    )

                                            },
                                            label = { Text(text = "name of Teacher") },
                                            textStyle = TextStyle(fontSize = FontSize.medium),
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
                                            textStyle = TextStyle(fontSize = FontSize.medium),
                                            colors = TextFieldDefaults.textFieldColors(
                                                containerColor = colorOfAllPairs
                                            ),
                                            label = { Text(text = "classrom") }

                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceAround,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(5.dp)
                                        ) {
                                            Row(modifier = Modifier.clickable {
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
                                            }) {
                                                Text(
                                                    text = lessonsMutableState[index].timeOfLesson.startTime.toShortString(),
                                                    fontSize = FontSize.big,
                                                )
                                                Icon(
                                                    imageVector = Icons.Filled.Schedule,
                                                    contentDescription = "time"
                                                )
                                            }
                                            Text(
                                                text = " - ",
                                                fontSize = FontSize.big
                                            )
                                            Row(modifier = Modifier.clickable {
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
                                            }) {
                                                Text(
                                                    text = lessonsMutableState[index].timeOfLesson.endTime.toShortString(),
                                                    fontSize = FontSize.big,
                                                )
                                                Icon(
                                                    imageVector = Icons.Filled.Schedule,
                                                    contentDescription = "time"
                                                )
                                            }
                                        }

                                        Box {
                                            Row(modifier = Modifier.clickable {
                                                expandedDaysOfWeekList = true
                                            }) {
                                                Text(
                                                    text = lessonsMutableState[index].dayOfThisPair.name,

                                                    fontSize = FontSize.big
                                                )
                                                if (expandedDaysOfWeekList) {
                                                    Icon(
                                                        imageVector = Icons.Filled.ExpandLess,
                                                        contentDescription = "expandLess"
                                                    )
                                                } else {
                                                    Icon(
                                                        imageVector = Icons.Filled.ExpandMore,
                                                        contentDescription = "expandMore"
                                                    )
                                                }

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
                                                            fontSize = FontSize.big
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
                                                    fontSize = FontSize.medium
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
                                                Text(
                                                    text = "Всегда",
                                                    fontSize = FontSize.medium
                                                )
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
                                                    fontSize = FontSize.medium
                                                )
                                            }
                                        }
                                    }
                                }

                                IconButton(onClick = {
                                    if (isEditing) {
                                        lessonsMutableState.writeDataToFile(context)
                                        editingSome.intValue -= 1
                                    } else {
                                        editingSome.intValue += 1
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


                            }

                        }
                    }
                }

                if (editingSome.intValue == 0) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp),
                        onClick = { adding = true },
//                            contentColor = mainColor,
                        backgroundColor = mainColor
                    ) {
                        Icon(Icons.Filled.Add, "Floating action button.")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    var newLesson by remember {
                        mutableStateOf(
                            Lesson(
                                DayOfWeek.MONDAY,
                                "",
                                "",
                                TimeOfLesson(0, 0, 0, 0),
                                "",
                                NumAndDen.Every
                            )
                        )
                    }
                    var expandedDaysOfWeekList by remember {
                        mutableStateOf(false)
                    }
                    TextField(
                        value = newLesson.nameOfSubject,
                        onValueChange = {
                            newLesson =
                                newLesson.copy(
                                    nameOfSubject = it
                                )
                        },
                        textStyle = TextStyle(fontSize = FontSize.big),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorOfAllPairs
                        ),
                        label = { Text(text = "name of lesson") },
                    )
                    TextField(
                        value = newLesson.nameOfTeacher,
                        onValueChange = {
                            newLesson =
                                newLesson.copy(
                                    nameOfTeacher = it
                                )
                        },
                        label = { Text(text = "name of Teacher") },
                        textStyle = TextStyle(fontSize = FontSize.small),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorOfAllPairs
                        ),
                    )
                    TextField(
                        value = newLesson.numberOfAud,
                        onValueChange = {
                            newLesson =
                                newLesson.copy(numberOfAud = it)

                        },
                        textStyle = TextStyle(fontSize = FontSize.medium),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = colorOfAllPairs
                        ),
                        label = { Text(text = "classrom") }
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(5.dp)
                    ) {
                        Row(modifier = Modifier.clickable {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    newLesson =
                                        newLesson.copy(
                                            timeOfLesson = TimeOfLesson(
                                                hour, minute,
                                                newLesson.timeOfLesson.endTime.hour,
                                                newLesson.timeOfLesson.endTime.minute,
                                            )
                                        )
                                },
                                newLesson.timeOfLesson.startTime.hour,
                                newLesson.timeOfLesson.startTime.minute,
                                true
                            ).show()
                        }) {
                            Text(
                                text = newLesson.timeOfLesson.startTime.toShortString(),
                                fontSize = FontSize.big,
                            )
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = "time"
                            )

                        }
                        Text(
                            text = " - ",
                            fontSize = FontSize.big
                        )
                        Row(modifier = Modifier.clickable {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->

                                    newLesson =
                                        newLesson.copy(
                                            timeOfLesson = TimeOfLesson(
                                                newLesson.timeOfLesson.startTime.hour,
                                                newLesson.timeOfLesson.startTime.minute,
                                                hour, minute,
                                            )
                                        )
                                },
                                newLesson.timeOfLesson.endTime.hour,
                                newLesson.timeOfLesson.endTime.minute,
                                true
                            ).show()
                        }) {
                            Text(
                                text = newLesson.timeOfLesson.endTime.toShortString(),
                                fontSize = FontSize.big,
                            )
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = "time"
                            )
                        }
                    }

                    Box {
                        Row(modifier = Modifier.clickable {
                            expandedDaysOfWeekList = true
                        }) {
                            Text(
                                text = newLesson.dayOfThisPair.name,
                                fontSize = FontSize.big
                            )
                            if (expandedDaysOfWeekList) {
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
                            expanded = expandedDaysOfWeekList,
                            onDismissRequest = {
                                expandedDaysOfWeekList = false
                            },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            for (day in daysOfWeek) {
                                Row {
                                    if (day == newLesson.dayOfThisPair) {
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
                                            newLesson.dayOfThisPair =
                                                day
                                            expandedDaysOfWeekList = false
                                        },
                                        fontSize = FontSize.big
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
                                newLesson =
                                    newLesson.copy(
                                        numeratorAndDenominator =
                                        NumAndDen.Numerator
                                    )
                            },
                            enabled = (newLesson.numeratorAndDenominator != NumAndDen.Numerator),
                            modifier = Modifier,
                        ) {
                            Text(
                                text = "Числитель",
                                fontSize = FontSize.medium
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                newLesson =
                                    newLesson.copy(
                                        numeratorAndDenominator =
                                        NumAndDen.Every
                                    )
                            },
                            enabled = (newLesson.numeratorAndDenominator != NumAndDen.Every),
                            modifier = Modifier,
                        ) {
                            Text(
                                text = "Всегда",
                                fontSize = FontSize.medium
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                newLesson =
                                    newLesson.copy(
                                        numeratorAndDenominator =
                                        NumAndDen.Denominator
                                    )
                            },
                            enabled = (newLesson.numeratorAndDenominator != NumAndDen.Denominator),
                            modifier = Modifier,
                        ) {
                            Text(
                                text = "Знаменатель",
                                fontSize = FontSize.medium
                            )
                        }
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = {
                                adding = false
                                lessonsMutableState.add(newLesson)
                                lessonsMutableState.sort()
                                lessonsMutableState.writeDataToFile(context)
                            }, modifier = Modifier.weight(1f),
                            enabled = (newLesson.nameOfSubject != "")
                        ) {
                            Text(text = "Добавить", fontSize = FontSize.medium)
                        }
                        Button(onClick = {
                            adding = false
                        }, modifier = Modifier.weight(1f)) {
                            Text(text = "Отмена", fontSize = FontSize.medium)
                        }

                    }
                }
            }
        }
        BottomBar(
            clickToMainScreen = clickToMainScreen,
            clickToScreenWithPickData = clickToScreenWithPickData,
            selected3 = true
        )
    }

}

