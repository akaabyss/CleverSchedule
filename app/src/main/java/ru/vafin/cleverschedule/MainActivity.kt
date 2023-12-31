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

class MainActivity : ComponentActivity() {
    private val mainScreen = "screen_1"
    private val screenWithPickData = "screen_2"
    private val editScreen = "screen_3"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController, startDestination = mainScreen
            ) {
                composable(mainScreen) {
                    MainScreen(
                        clickToScreenWithPickData = { navController.navigate(screenWithPickData) },
                        clickToEditScreen = { navController.navigate(editScreen) },
                    )
                }
                composable(screenWithPickData) {
                    ScreenWithPickData(
                        clickToMainScreen = {
                            navController.popBackStack()
                            navController.popBackStack()
                            navController.navigate(mainScreen)
                        }, clickToEditScreen = {
                            navController.popBackStack()
                            navController.navigate(editScreen)
                        }
                    )
                }
                composable(editScreen) {
                    EditScreen(
                        clickToMainScreen = {
                            navController.popBackStack()
                            navController.popBackStack()
                            navController.navigate(mainScreen)
                        }, clickToScreenWithPickData = {
                            navController.popBackStack()
                            navController.navigate(screenWithPickData)
                        }
                    )
                }
            }
        }
    }

}