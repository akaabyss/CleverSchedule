package ru.vafin.fiit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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

}