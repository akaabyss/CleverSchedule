package ru.vafin.cleverschedule.screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.vafin.cleverschedule.BottomBar
import ru.vafin.cleverschedule.Lesson
import ru.vafin.cleverschedule.editScreen
import ru.vafin.cleverschedule.getLessonsFromListString
import ru.vafin.cleverschedule.mainScreen
import ru.vafin.cleverschedule.readData
import ru.vafin.cleverschedule.ui.theme.mainColor
import ru.vafin.cleverschedule.writeDataToFile

@SuppressLint("MutableCollectionMutableState", "ServiceCast")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWithPickData(
    navController: NavController,
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
                    text = "Update local database",
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
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("label", textForCopy)
                    clipboard.setPrimaryClip(clip)
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
        BottomBar(
            clickToMainScreen = {
                navController.popBackStack()
                navController.popBackStack()
                navController.navigate(mainScreen)
            }, clickToEditScreen = {
                navController.popBackStack()
                navController.navigate(editScreen)
            },
            selected2 = true
        )
    }

}
