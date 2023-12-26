package ru.vafin.fiit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.vafin.fiit.ui.theme.mainColor

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
            icon = { Icon(Icons.Default.UploadFile, contentDescription = "Icon2") },
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