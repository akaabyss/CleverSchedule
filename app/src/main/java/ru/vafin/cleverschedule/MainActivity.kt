package ru.vafin.cleverschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.vafin.cleverschedule.screens.EditScreen
import ru.vafin.cleverschedule.screens.MainScreen
import ru.vafin.cleverschedule.screens.ScreenWithPickData

const val mainScreen = "screen_1"
const val screenWithPickData = "screen_2"
const val editScreen = "screen_3"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()
            NavHost(
                navController = navController, startDestination = mainScreen
            ) {
                composable(mainScreen) { MainScreen(navController = navController) }
                composable(screenWithPickData) { ScreenWithPickData(navController = navController) }
                composable(editScreen) { EditScreen(navController = navController) }
            }
            }

        }


}