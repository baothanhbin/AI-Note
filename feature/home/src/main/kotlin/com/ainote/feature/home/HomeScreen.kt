package com.ainote.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainote.core.designsystem.theme.AINoteThemeExtras
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
            // Elegant Dashboard Header
            DashboardHeader(onGraphClick = onGraphClick, onSearchClick = onSearchClick)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        androidx.compose.material3.CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is HomeUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp) // space for FAB
                    ) {
                        item {
                            InsightsRow(totalNotes = uiState.totalNotesCount)
                        }

                        item {
                            QuickActionsRow(
                                onAddNoteClick = onAddNoteClick,
                                onAskAiClick = { /* TODO Phase 4 */ }
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = uiState.searchQuery,
                                onValueChange = onSearchQueryChange,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                placeholder = { Text("Find ideas, tags, or content...") },
                                leadingIcon = {
                                    Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                },
                                shape = MaterialTheme.shapes.large,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true
                            )
                        }

                        item {
                            HomeToolbar(
                                sortOrder = uiState.sortOrder,
                                isGridView = uiState.isGridView,
                                onSortOrderChange = onSortOrderChange,
                                onToggleGridView = onToggleGridView
                            )
                        }

                        if (uiState.notes.isEmpty() && uiState.pinnedNotes.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(48.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (uiState.searchQuery.isNotBlank()) "No notes match your search." else "A quiet place for your thoughts.\nTap + to start.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        } else {
                            if (uiState.pinnedNotes.isNotEmpty()) {
                                item { SectionHeader("Pinned") }
                                if (uiState.isGridView) {
                                    item {
                                        NoteGrid(
                                            notes = uiState.pinnedNotes,
                                            onNoteClick = onNoteClick
                                        )
                                    }
                                } else {
                                    items(uiState.pinnedNotes, key = { it.id }) { note ->
                                        NoteCard(
                                            note = note,
                                            modifier = Modifier
                                                .padding(horizontal = 24.dp, vertical = 6.dp)
                                                .clickable { onNoteClick(note.id) }
                                        )
                                    }
                                }
                            }

                            if (uiState.notes.isNotEmpty()) {
                                item { SectionHeader("Recent Notes") }
                                if (uiState.isGridView) {
                                    item {
                                        NoteGrid(
                                            notes = uiState.notes,
                                            onNoteClick = onNoteClick
                                        )
                                    }
                                } else {
                                    items(uiState.notes, key = { it.id }) { note ->
                                        NoteCard(
                                            note = note,
                                            modifier = Modifier
                                                .padding(horizontal = 24.dp, vertical = 6.dp)
                                                .clickable { onNoteClick(note.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is HomeUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardHeader(onGraphClick: () -> Unit, onSearchClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Knowledge Vault",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Your second brain",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = onGraphClick) {
                Icon(Icons.Default.AccountTree, contentDescription = "Graph View", tint = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun InsightsRow(totalNotes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InsightCard(
            title = "Total Notes",
            value = totalNotes.toString(),
            icon = Icons.Default.Description
        )
        InsightCard(
            title = "Connections",
            value = "0", // Placeholder for phase 3
            icon = Icons.Default.AccountTree
        )
    }
}

@Composable
fun InsightCard(title: String, value: String, icon: ImageVector) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.width(140.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionsRow(onAddNoteClick: () -> Unit, onAskAiClick: () -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = false,
                onClick = onAddNoteClick,
                label = { Text("New Note") },
                leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
        }
        item {
            FilterChip(
                selected = false,
                onClick = onAskAiClick,
                label = { Text("Ask AI") },
                leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun HomeToolbar(
    sortOrder: SortOrder,
    isGridView: Boolean,
    onSortOrderChange: (SortOrder) -> Unit,
    onToggleGridView: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { showSortMenu = true }) {
                Icon(Icons.Default.Sort, contentDescription = "Sort", modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = sortOrderEntries.find { it.first == sortOrder }?.second ?: "Sort",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                contentDescription = if (isGridView) "List view" else "Grid view",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private val sortOrderEntries = listOf(
    SortOrder.DateDesc to "Newest first",
    SortOrder.DateAsc to "Oldest first",
    SortOrder.TitleAsc to "A-Z",
    SortOrder.TitleDesc to "Z-A"
)

// Helper internal grid component to embed in LazyColumn
@Composable
private fun NoteGrid(notes: List<Note>, onNoteClick: (String) -> Unit) {
    // Due to nesting scrollables (LazyVerticalGrid in LazyColumn), we have to simulate a grid.
    // A simpler way is chunking the notes list into rows.
    val chunked = notes.chunked(2)
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
        chunked.forEach { rowNotes ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowNotes.forEach { note ->
                    Box(modifier = Modifier.weight(1f)) {
                        NoteCard(
                            note = note,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { onNoteClick(note.id) }
                        )
                    }
                }
                // Handle odd items
                if (rowNotes.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
