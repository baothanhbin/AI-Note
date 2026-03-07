package com.baothanhbin.ainote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ainote.feature.editor.EditorRoute
import com.ainote.feature.home.HomeRoute
import com.ainote.feature.search.SearchRoute
import com.baothanhbin.ainote.ui.theme.AINoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AINoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "home_route"
                    ) {
                        composable("home_route") {
                            HomeRoute(
                                onNoteClick = { noteId ->
                                    navController.navigate("editor_route?noteId=$noteId")
                                },
                                onAddNoteClick = {
                                    navController.navigate("editor_route")
                                },
                                onSearchClick = {
                                    navController.navigate("search_route")
                                }
                            )
                        }
                        
                        composable(
                            route = "editor_route?noteId={noteId}",
                            arguments = listOf(
                                navArgument("noteId") {
                                    type = NavType.StringType
                                    nullable = true
                                    defaultValue = null
                                }
                            )
                        ) {
                            EditorRoute(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        
                        composable("search_route") {
                            SearchRoute(
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                onNoteClick = { noteId ->
                                    navController.navigate("editor_route?noteId=$noteId")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}