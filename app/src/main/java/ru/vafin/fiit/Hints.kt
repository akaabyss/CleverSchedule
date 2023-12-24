package ru.vafin.fiit

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextHint(text:String) {
    Row() {
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, fontSize = 10.sp)
    }
}
