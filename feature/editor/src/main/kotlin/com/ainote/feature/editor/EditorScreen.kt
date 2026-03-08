package com.ainote.feature.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainote.core.model.note.Note
import com.ainote.core.model.note.Tag
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material3.RichText

@Composable
fun EditorRoute(
    onBackClick: () -> Unit,
    onNoteClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: EditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    EditorScreen(
        uiState = uiState,
        onBackClick = {
            focusManager.clearFocus()
            viewModel.saveNote(onSaveSuccess = onBackClick)
        },
        onTitleChange = viewModel::updateTitle,
        onContentChange = viewModel::updateContent,
        onTogglePin = viewModel::togglePin,
        onAddTag = viewModel::addTag,
        onRemoveTag = viewModel::removeTag,
        onInsertChecklist = viewModel::insertChecklist,
        onInsertCodeBlock = viewModel::insertCodeBlock,
        onNoteClick = onNoteClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditorScreen(
    uiState: EditorUiState,
    onBackClick: () -> Unit,
    onNoteClick: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onTogglePin: () -> Unit,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    onInsertChecklist: () -> Unit,
    onInsertCodeBlock: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isPinned by remember { mutableStateOf(false) }
    var renderMarkdown by remember { mutableStateOf(false) }
    var isPreviewMode by remember { mutableStateOf(false) }
    var showTagSheet by remember { mutableStateOf(false) }
    var tagSearchQuery by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is EditorUiState.Content) {
            title = uiState.title
            content = uiState.content
            isPinned = uiState.isPinned
            renderMarkdown = uiState.renderMarkdown
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (renderMarkdown) {
                        IconButton(onClick = { isPreviewMode = !isPreviewMode }) {
                            Icon(
                                imageVector = if (isPreviewMode) Icons.Filled.Edit else Icons.Filled.Info,
                                contentDescription = if (isPreviewMode) "Edit mode" else "Preview markdown"
                            )
                        }
                    }
                    IconButton(onClick = onTogglePin) {
                        Icon(
                            imageVector = Icons.Filled.PushPin,
                            contentDescription = if (isPinned) "Unpin" else "Pin",
                            tint = if (isPinned) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (uiState is EditorUiState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator()
                }
            } else {
                if (showTagSheet && uiState is EditorUiState.Content) {
                    TagSelectionBottomSheet(
                        allTags = uiState.allAvailableTags,
                        currentTagIds = uiState.tags.map { it.id }.toSet(),
                        searchQuery = tagSearchQuery,
                        onSearchQueryChange = { tagSearchQuery = it },
                        onTagSelect = { tagName ->
                            onAddTag(tagName)
                            showTagSheet = false
                            tagSearchQuery = ""
                        },
                        onDismiss = {
                            showTagSheet = false
                            tagSearchQuery = ""
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    TextField(
                        value = title,
                        onValueChange = {
                            title = it
                            onTitleChange(it)
                        },
                        placeholder = { Text("Untitled", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
                        textStyle = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )

                    if (uiState is EditorUiState.Content) {
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.tags.forEach { tag ->
                                InputChip(
                                    selected = false,
                                    onClick = { onRemoveTag(tag.id) },
                                    label = { Text(tag.name) },
                                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Remove", modifier = Modifier.size(16.dp)) }
                                )
                            }
                            AssistChip(
                                onClick = { showTagSheet = true },
                                label = { Text("Add Tag") },
                                leadingIcon = { Icon(Icons.Default.Add, contentDescription = "Add Tag", modifier = Modifier.size(18.dp)) }
                            )
                        }
                    }

                    if (isPreviewMode) {
                        Surface(
                            modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp),
                            color = Color.Transparent
                        ) {
                            RichText {
                                Markdown(content)
                            }
                        }
                    } else {
                        TextField(
                            value = content,
                            onValueChange = {
                                content = it
                                onContentChange(it)
                            },
                            placeholder = { Text("Write your note...") },
                            textStyle = MaterialTheme.typography.bodyLarge,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                    }

                    if (uiState is EditorUiState.Content && (uiState.backlinks.isNotEmpty() || uiState.outgoingLinks.isNotEmpty())) {
                        ConnectionsSection(
                            backlinks = uiState.backlinks,
                            outgoingLinks = uiState.outgoingLinks,
                            onNoteClick = onNoteClick
                        )
                    }
                    
                    // Space for floating toolbar
                    Spacer(modifier = Modifier.padding(bottom = 80.dp))
                }
                
                // Floating Toolbar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                ) {
                    EditorToolbar(
                        onInsertChecklist = onInsertChecklist,
                        onInsertCodeBlock = onInsertCodeBlock,
                        onAddTag = { showTagSheet = true }
                    )
                }
            }
        }
    }
}

@Composable
private fun EditorToolbar(
    onInsertChecklist: () -> Unit,
    onInsertCodeBlock: () -> Unit,
    onAddTag: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onInsertChecklist, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.CheckBox, contentDescription = "Checklist")
            }
            IconButton(onClick = onInsertCodeBlock, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Code, contentDescription = "Code Block")
            }
            IconButton(onClick = onAddTag, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Label, contentDescription = "Tag")
            }
            // AI Action placeholder
            IconButton(onClick = { /* TODO Phase 2 */ }, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Info, contentDescription = "Ask AI", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun ConnectionsSection(
    backlinks: List<Note>,
    outgoingLinks: List<Note>,
    onNoteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 16.dp)) {
        HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))
        Text(
            "Related notes",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (backlinks.isNotEmpty()) {
            Text(
                "Linked from",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            backlinks.forEach { link ->
                LinkCard(
                    title = link.title.ifBlank { "Untitled" },
                    snippet = link.content.take(80),
                    onClick = { onNoteClick(link.id) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        if (outgoingLinks.isNotEmpty()) {
            Text(
                "Links to",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            outgoingLinks.forEach { link ->
                LinkCard(
                    title = link.title.ifBlank { "Untitled" },
                    snippet = link.content.take(80),
                    onClick = { onNoteClick(link.id) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagSelectionBottomSheet(
    allTags: List<Tag>,
    currentTagIds: Set<String>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTagSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val availableToAdd = allTags.filter { it.id !in currentTagIds }
    val filtered = if (searchQuery.isBlank()) availableToAdd
    else availableToAdd.filter { it.name.lowercase().contains(searchQuery.lowercase()) }
    val canCreateNew = searchQuery.isNotBlank() && availableToAdd.none { it.name.equals(searchQuery.trim(), ignoreCase = true) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                "Add tag",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search or create tag") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
            if (canCreateNew) {
                FilledTonalButton(
                    onClick = { onTagSelect(searchQuery.trim()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text("Create tag: ${searchQuery.trim()}")
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filtered, key = { it.id }) { tag ->
                    androidx.compose.material3.ListItem(
                        headlineContent = { Text(tag.name) },
                        modifier = Modifier.clickable { onTagSelect(tag.name) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LinkCard(
    title: String,
    snippet: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Link,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1
                )
                if (snippet.isNotBlank()) {
                    Text(
                        snippet,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
