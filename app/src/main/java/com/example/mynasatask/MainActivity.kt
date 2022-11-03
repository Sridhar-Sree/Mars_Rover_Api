package com.example.mynasatask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mynasatask.screens.details.MarsDetailScreen
import com.example.mynasatask.screens.home.HomeScreen
import com.example.mynasatask.ui.theme.MyNasaTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNasaTaskTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home_list_screen"
                ) {
                    composable("home_list_screen") {
                        HomeScreen(navController = navController)
                    }


                    composable(
                        "mars_detail_screen/{number}",
                        arguments = listOf(
                            navArgument("number") {
                                type = NavType.IntType
                            }
                        )
                    )

                    {
                        val marsid = remember {
                            it.arguments?.getInt("number")
                        }
                        if (marsid != null) {
                            MarsDetailScreen(
                                marsid = marsid,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
