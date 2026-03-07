package com.ainote.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onSettingsClick: () -> Unit,
    onGraphClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
        onSearchClick = onSearchClick,
        onSettingsClick = onSettingsClick,
        onGraphClick = onGraphClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSortOrderChange = viewModel::setSortOrder,
        onToggleGridView = viewModel::toggleGridView,
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
    onSettingsClick: () -> Unit,
    onGraphClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    onToggleGridView: () -> Unit,
    onTogglePin: (Note) -> Unit,
    onArchive: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("AI Note", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = onGraphClick) {
                        Icon(Icons.Default.AccountTree, contentDescription = "Graph View")
                    }
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search notes")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }
                is HomeUiState.Success -> {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Search notes...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        singleLine = true
                    )
                    HomeToolbar(
                        sortOrder = uiState.sortOrder,
                        isGridView = uiState.isGridView,
                        onSortOrderChange = onSortOrderChange,
                        onToggleGridView = onToggleGridView
                    )
                    if (uiState.notes.isEmpty() && uiState.pinnedNotes.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (uiState.searchQuery.isNotBlank())
                                    "No notes match your search."
                                else
                                    "No notes yet. Tap + to create one.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        if (uiState.isGridView) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                if (uiState.pinnedNotes.isNotEmpty()) {
                                    item(span = { GridItemSpan(2) }) {
                                        Text(
                                            "Pinned",
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                    }
                                    items(uiState.pinnedNotes, key = { it.id }) { note ->
                                        NoteCard(
                                            note = note,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onNoteClick(note.id) }
                                        )
                                    }
                                }
                                if (uiState.notes.isNotEmpty()) {
                                    item(span = { GridItemSpan(2) }) {
                                        Text(
                                            "Notes",
                                            style = MaterialTheme.typography.labelMedium,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                    items(uiState.notes, key = { it.id }) { note ->
                                        NoteCard(
                                            note = note,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { onNoteClick(note.id) }
                                        )
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
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
                }
                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeToolbar(
    sortOrder: SortOrder,
    isGridView: Boolean,
    onSortOrderChange: (SortOrder) -> Unit,
    onToggleGridView: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            IconButton(onClick = { showSortMenu = true }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort")
            }
            androidx.compose.material3.DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                sortOrderEntries.forEach { (order, label) ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            onSortOrderChange(order)
                            showSortMenu = false
                        }
                    )
                }
            }
        }
        IconButton(onClick = onToggleGridView) {
            Icon(
                imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                contentDescription = if (isGridView) "List view" else "Grid view"
            )
        }
    }
}

private val sortOrderEntries = listOf(
    SortOrder.DateDesc to "Newest first",
    SortOrder.DateAsc to "Oldest first",
    SortOrder.TitleAsc to "Title A-Z",
    SortOrder.TitleDesc to "Title Z-A"
)
