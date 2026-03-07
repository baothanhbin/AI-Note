package com.ainote.feature.tags

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainote.core.model.note.Note
import com.ainote.core.model.note.Tag
import com.ainote.core.ui.components.NoteCard

@Composable
fun TagsRoute(
    onNoteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TagsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TagsScreen(
        uiState = uiState,
        onNoteClick = onNoteClick,
        onTagSelect = viewModel::selectTag,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TagsScreen(
    uiState: TagsUiState,
    onNoteClick: (String) -> Unit,
    onTagSelect: (Tag?) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Tags") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is TagsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }
                is TagsUiState.Success -> {
                    TagChipsRow(
                        tags = uiState.tags,
                        selectedTag = uiState.selectedTag,
                        onTagSelect = onTagSelect,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    if (uiState.selectedTag != null) {
                        NotesByTagList(
                            notes = uiState.notesByTag,
                            onNoteClick = onNoteClick,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            com.ainote.core.ui.component.EmptyStateView(
                                title = "Tags",
                                message = "Select a tag to view notes",
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TagChipsRow(
    tags: List<Tag>,
    selectedTag: Tag?,
    onTagSelect: (Tag?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedTag == null,
                onClick = { onTagSelect(null) },
                label = { Text("All") }
            )
        }
        items(tags, key = { it.id }) { tag ->
            FilterChip(
                selected = selectedTag?.id == tag.id,
                onClick = { onTagSelect(tag) },
                label = { Text(tag.name) }
            )
        }
    }
}

@Composable
private fun NotesByTagList(
    notes: List<Note>,
    onNoteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (notes.isEmpty()) {
        com.ainote.core.ui.component.EmptyStateView(
            title = "No notes",
            message = "No notes with this tag",
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes, key = { it.id }) { note ->
                NoteCard(
                    note = note,
                    modifier = Modifier.clickable { onNoteClick(note.id) }
                )
            }
        }
    }
}
