package com.applabs.eventx.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.applabs.eventx.details.presentation.DetailsScreen
import com.applabs.eventx.events.presentation.EventListViewModel
import com.applabs.eventx.events.presentation.NewEventScreenForm
import com.applabs.eventx.events.util.Screen
import com.applabs.eventx.ui.theme.EventXTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventXTheme {
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    val eventListViewModel = hiltViewModel<EventListViewModel>()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController, eventListViewModel)
                        }

                        composable(
                            Screen.Details.route + "/{eventId}",
                            arguments = listOf(
                                navArgument("eventId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            DetailsScreen(navHostController = navController)
                        }

                        composable(
                            Screen.NewEvent.route
                        ) { backStackEntry ->
                            NewEventScreenForm(
                                navHostController = navController,
                                eventListViewModel = eventListViewModel
                            )
                        }
                    }

                }
            }
        }
    }


    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}

