package com.baothanhbin.ainote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ainote.feature.editor.EditorRoute
import com.ainote.feature.home.HomeRoute
import com.ainote.feature.search.SearchRoute
import com.ainote.feature.settings.SettingsRoute
import com.ainote.feature.graph.GraphRoute
import com.ainote.feature.tags.TagsRoute
import com.baothanhbin.ainote.navigation.AINoteBottomBar
import com.baothanhbin.ainote.ui.theme.AINoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            
            val useDarkTheme = when (uiState) {
                is MainActivityUiState.Loading -> false
                is MainActivityUiState.Success -> (uiState as MainActivityUiState.Success).useDarkMode
            }

            AINoteTheme(darkTheme = useDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = { AINoteBottomBar(navController) }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home_route",
                            modifier = Modifier.padding(innerPadding)
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
                                    },
                                    onSettingsClick = {
                                        navController.navigate("settings_route")
                                    },
                                    onGraphClick = {
                                        navController.navigate("graph_route")
                                    }
                                )
                            }

                            composable("tags_route") {
                                TagsRoute(
                                    onNoteClick = { noteId ->
                                        navController.navigate("editor_route?noteId=$noteId")
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
                                    },
                                    onNoteClick = { noteId ->
                                        navController.popBackStack()
                                        navController.navigate("editor_route?noteId=$noteId")
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

                            composable("settings_route") {
                                SettingsRoute(
                                    onBackClick = {
                                        navController.popBackStack()
                                    }
                                )
                            }

                            composable("graph_route") {
                                GraphRoute(
                                    onBackClick = {
                                        navController.popBackStack()
                                    },
                                    onNodeClick = { noteId ->
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
}