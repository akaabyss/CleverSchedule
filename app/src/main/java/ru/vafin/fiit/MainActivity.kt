package ru.vafin.fiit

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.content.ClipData
import android.content.ClipboardManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.vafin.fiit.ui.theme.colorOfAllPairs
import ru.vafin.fiit.ui.theme.colorOfThisPair
import ru.vafin.fiit.ui.theme.mainColor
import java.time.DayOfWeek
import java.time.LocalTime
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    private val screen1 = "screen_1"
    private val screen2 = "screen_2"
    private val screen3 = "screen_3"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            mutableStateListOf(*(readData(this).toTypedArray()))
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
                                                fontSize = FontSize.fontInTheCardOfTheNameOfSubject
                                            )
                                            if (lesson.nameOfTeacher != "") {
                                                Text(
                                                    text = lesson.nameOfTeacher,
                                                    fontSize = FontSize.fontInTheCardOfTheNameOfTheTeacher
                                                )
                                            }
                                            if (lesson.numberOfAud != "") {
                                                Text(
                                                    text = lesson.numberOfAud,
                                                    fontSize = FontSize.fontInTheCardOfTheNumberOfTheClassRoom
                                                )
                                            }
                                            Text(
                                                text = "${lesson.timeOfLesson.startTime.toShortString()} - ${lesson.timeOfLesson.endTime.toShortString()}",
                                                fontSize = FontSize.fontInTheCardOfTheTimeOfLesson
                                            )
                                            Text(
                                                text = "${lesson.dayOfThisPair} : ${
                                                    getStringWithNameByNumOrDen(
                                                        lesson.numeratorAndDenominator
                                                    )
                                                }",
                                                fontSize = FontSize.fontInTheCardOfTheDayOfWeekAndNumAndDen
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
                                                textStyle = TextStyle(fontSize = FontSize.expandedFontInTheCardOfTheNameOfSubject),
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
                                                textStyle = TextStyle(fontSize = FontSize.expandedFontInTheCardOfTheNameOfTheTeacher),
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
                                                textStyle = TextStyle(fontSize = FontSize.expandedFontInTheCardOfTheNumberOfTheClassRoom),
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
                                                        fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson,
                                                    )
                                                    TextHint(text = "click")
                                                }
                                                Text(
                                                    text = " - ",
                                                    fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson
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
                                                        fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson,
                                                    )
                                                    TextHint(text = "click")
                                                }
                                            }

                                            Box {
                                                Row(modifier = Modifier.clickable {
                                                    expandedDaysOfWeekList = true
                                                }) {
                                                    Text(
                                                        text = lessonsMutableState[index].dayOfThisPair.name,

                                                        fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson
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
                                                                fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson
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
                                                        fontSize = FontSize.fontForButtons
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
                                                        fontSize = FontSize.fontForButtons
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
                                                        fontSize = FontSize.fontForButtons
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
                                .align(Alignment.BottomCenter)
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
                            textStyle = TextStyle(fontSize = FontSize.expandedFontInTheCardOfTheNameOfSubject),
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
                            textStyle = TextStyle(fontSize = FontSize.expandedFontInTheCardOfTheNameOfTheTeacher),
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
                            textStyle = TextStyle(fontSize = FontSize.expandedFontInTheCardOfTheNumberOfTheClassRoom),
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
                                    fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson,
                                )
                                TextHint(text = "click")
                            }
                            Text(
                                text = " - ",
                                fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson
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
                                    fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson,
                                )
                                TextHint(text = "click")
                            }
                        }

                        Box {
                            Row(modifier = Modifier.clickable {
                                expandedDaysOfWeekList = true
                            }) {
                                Text(
                                    text = newLesson.dayOfThisPair.name,
                                    fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson
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
                                            fontSize = FontSize.expandedFontInTheCardOfTheTimeOfLesson
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
                                    fontSize = FontSize.fontForButtons
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
                                    fontSize = FontSize.fontForButtons
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
                                    fontSize = FontSize.fontForButtons
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
                                Text(text = "Добавить", fontSize = FontSize.fontForButtons)
                            }
                            Button(onClick = {
                                adding = false
                            }, modifier = Modifier.weight(1f)) {
                                Text(text = "Отмена", fontSize = FontSize.fontForButtons)
                            }

                        }
                    }
                }
            }
            BottomBar(
                onClickToScreen1, onClickToScreen2, onClickToScreen3, selected3 = true
            )
        }

    }

    @SuppressLint("MutableCollectionMutableState", "ServiceCast")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScreenWithPickData(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {
        val context = LocalContext.current
        var text by remember { mutableStateOf("") }
        var lessons: SnapshotStateList<Lesson>

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = mainColor,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Выгрузить список пар",
                        fontSize = 22.sp
                    )
                }
            }
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .weight(10f),
                label = { Text(text = "splittable text") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        var textForCopy = ""
                        for (lesson in readData(context)) {
                            textForCopy += lesson.toFileString() + "\n"
                        }
                        val clipboardManager =
                            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("text", textForCopy)
                        clipboardManager.setPrimaryClip(clipData)
                    }
                ) {
                    Text(text = "copy lessons")
                }
                Button(
                    onClick = {
                        lessons =
                            mutableStateListOf(*(getLessonsFromListString(text.split("\n")).toTypedArray()))
                        text = ""
                        lessons.sort()
                        lessons.writeDataToFile(context)

                    }, enabled = text.isNotEmpty()
                ) {
                    Text(text = "update lessons")
                }
            }


//                    var text = "И сделать кнопку \"скопировать\""
//                Text(text = text)
//                        Button(onClick = {
//                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                    val clip: ClipData = ClipData.newPlainText("", text)
//                    clipboard.setPrimaryClip(clip)
//
//                }) {
//                    Text(text = "copy")
//                }
            BottomBar(
                onClickToScreen1, onClickToScreen2, onClickToScreen3, selected2 = true
            )
        }

    }

    @OptIn(ExperimentalStdlibApi::class)
    @Composable
    fun MainScreen(
        onClickToScreen1: () -> Unit,
        onClickToScreen2: () -> Unit,
        onClickToScreen3: () -> Unit,
    ) {
        val lessons = remember {
            mutableStateListOf(*(readData(this).toTypedArray()))
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.softpaw),
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(30.dp) //форма
                            )
                            Text(text = "@akasoftpaw", fontSize = 12.sp)
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
                        for (indexOfLesson in 0..lessonsOfThisDay.lastIndex) {
                            val thisLesson = lessonsOfThisDay[indexOfLesson]
                            if (thisLesson.timeOfLesson.timeVnutri(time)) {
                                thisLesson.GetStringForSchedule(colorOfThisPair)
                            } else {
                                thisLesson.GetStringForSchedule(colorOfAllPairs)
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(20.dp))
//                        for (i in 1..10) {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalArrangement = Arrangement.Center
//                            ) {
//                                Text(text = "Weekends? Rest after death!", fontSize = 17.sp)
//                                Spacer(modifier = Modifier.height(5.dp))
//                            }

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
                                text = "тыкай по экрану",
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
                onClickToScreen1, onClickToScreen2, onClickToScreen3, selected1 = true
            )
        }

    }

}