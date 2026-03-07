package com.ainote.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainote.core.model.note.Note
import com.ainote.core.ui.components.NoteCard

@Composable
fun HomeRoute(
    onNoteClick: (String) -> Unit,
    onAddNoteClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    HomeScreen(
        uiState = uiState,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
        onSearchClick = onSearchClick,
        onTogglePin = viewModel::togglePin,
        onArchive = viewModel::archiveNote,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onNoteClick: (String) -> Unit,
    onAddNoteClick: () -> Unit,
    onSearchClick: () -> Unit,
    onTogglePin: (Note) -> Unit,
    onArchive: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("AI Note") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(androidx.compose.material.icons.Icons.Filled.Search, contentDescription = "Search notes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is HomeUiState.Success -> {
                    if (uiState.notes.isEmpty() && uiState.pinnedNotes.isEmpty()) {
                        Text(
                            text = "No notes yet. Tap + to create one.",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (uiState.pinnedNotes.isNotEmpty()) {
                                item { 
                                    Text(
                                        "Pinned", 
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    ) 
                                }
                                items(uiState.pinnedNotes, key = { it.id }) { note ->
                                    NoteCard(
                                        note = note,
                                        modifier = Modifier.clickable { onNoteClick(note.id) }
                                    )
                                }
                            }
                            
                            if (uiState.notes.isNotEmpty()) {
                                item { 
                                    Text(
                                        "Notes", 
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) 
                                }
                                items(uiState.notes, key = { it.id }) { note ->
                                    NoteCard(
                                        note = note,
                                        modifier = Modifier.clickable { onNoteClick(note.id) }
                                    )
                                }
                            }
                        }
                    }
                }
                is HomeUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
