package ru.vafin.fiit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextHint(text:String){

    Spacer(modifier = Modifier.width(3.dp))
    Text(text = text,
        fontSize = FontSize.extraSmall)
}