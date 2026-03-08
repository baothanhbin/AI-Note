package com.ainote.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainote.core.model.note.Note
import com.ainote.domain.usecase.note.GetNoteByIdUseCase
import com.ainote.domain.usecase.note.SaveNoteUseCase
import com.ainote.domain.usecase.note.UpdateNoteUseCase
import com.ainote.domain.usecase.tag.GetAllTagsUseCase
import com.ainote.domain.usecase.tag.GetTagsForNoteUseCase
import com.ainote.domain.usecase.tag.AddTagToNoteUseCase
import com.ainote.domain.usecase.tag.RemoveTagFromNoteUseCase
import com.ainote.domain.usecase.link.GetBacklinksUseCase
import com.ainote.domain.usecase.link.GetOutgoingLinksUseCase
import com.ainote.domain.usecase.link.ProcessNoteLinksUseCase
import com.ainote.domain.usecase.user.GetUseMarkdownPreviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.ainote.domain.ai.usecase.SuggestTitleUseCase
import com.ainote.domain.ai.usecase.SummarizeNoteUseCase
import com.ainote.domain.ai.usecase.ExtractTagsUseCase
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

sealed interface EditorUiState {
    data object Loading : EditorUiState
    data class Content(
        val title: String,
        val content: String,
        val isPinned: Boolean,
        val renderMarkdown: Boolean = false,
        val backlinks: List<Note> = emptyList(),
        val outgoingLinks: List<Note> = emptyList(),
        val tags: List<com.ainote.core.model.note.Tag> = emptyList(),
        val allAvailableTags: List<com.ainote.core.model.note.Tag> = emptyList(),
        val isGeneratingAi: Boolean = false
    ) : EditorUiState
}

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val processNoteLinksUseCase: ProcessNoteLinksUseCase,
    private val getBacklinksUseCase: GetBacklinksUseCase,
    private val getOutgoingLinksUseCase: GetOutgoingLinksUseCase,
    private val getTagsForNoteUseCase: GetTagsForNoteUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val addTagToNoteUseCase: AddTagToNoteUseCase,
    private val removeTagFromNoteUseCase: RemoveTagFromNoteUseCase,
    private val suggestTitleUseCase: SuggestTitleUseCase,
    private val summarizeNoteUseCase: SummarizeNoteUseCase,
    private val extractTagsUseCase: ExtractTagsUseCase,
    getUseMarkdownPreviewUseCase: GetUseMarkdownPreviewUseCase
) : ViewModel() {

    private val noteId: String? = savedStateHandle["noteId"]
    private var isEditMode = noteId != null
    private val currentNoteId = noteId ?: UUID.randomUUID().toString()
    private var currentNote: Note? = null

    private val _title = MutableStateFlow("")
    private val _content = MutableStateFlow("")
    private val _isPinned = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(isEditMode)

    private val _isGeneratingAi = MutableStateFlow(false)

    private val backlinksFlow = if (noteId != null) getBacklinksUseCase(noteId) else flowOf(emptyList())
    private val outgoingLinksFlow = if (noteId != null) getOutgoingLinksUseCase(noteId) else flowOf(emptyList())
    private val tagsFlow = getTagsForNoteUseCase(currentNoteId)
    private val allTagsFlow = getAllTagsUseCase()

    val uiState: StateFlow<EditorUiState> = combine(
        combine(_title, _content, _isPinned, _isLoading, getUseMarkdownPreviewUseCase()) { t, c, p, l, m ->
            Triple(Triple(t, c, p), l, m)
        },
        combine(backlinksFlow, outgoingLinksFlow, tagsFlow, allTagsFlow, _isGeneratingAi) { b, o, t, a, ai ->
            listOf(b, o, t, a, ai)
        }
    ) { (params, isLoading, useMarkdown), lists ->
        val (title, content, isPinned) = params
        val backlinks = lists[0] as List<Note>
        val outgoingLinks = lists[1] as List<Note>
        val tags = lists[2] as List<com.ainote.core.model.note.Tag>
        val allTags = lists[3] as List<com.ainote.core.model.note.Tag>
        val isGeneratingAi = lists[4] as Boolean

        if (isLoading) {
            EditorUiState.Loading
        } else {
            EditorUiState.Content(title, content, isPinned, useMarkdown, backlinks, outgoingLinks, tags, allTags, isGeneratingAi)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EditorUiState.Loading
    )

    init {
        if (noteId != null) {
            viewModelScope.launch {
                getNoteByIdUseCase(noteId).collect { note ->
                    if (note != null) {
                        currentNote = note
                        _title.value = note.title
                        _content.value = note.content
                        _isPinned.value = note.isPinned
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }

    fun togglePin() {
        _isPinned.value = !_isPinned.value
    }

    fun insertChecklist() {
        val suffix = if (_content.value.endsWith("\n") || _content.value.isEmpty()) "" else "\n"
        _content.update { it + "${suffix}- [ ] " }
    }

    fun insertCodeBlock() {
        val suffix = if (_content.value.endsWith("\n") || _content.value.isEmpty()) "" else "\n"
        _content.update { it + "${suffix}```\n\n```" }
    }

    fun suggestTitle() {
        val content = _content.value
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _isGeneratingAi.update { true }
            val result = suggestTitleUseCase(content)
            result.onSuccess { generatedTitle ->
                _title.value = generatedTitle
            }
            _isGeneratingAi.update { false }
        }
    }

    fun summarizeNote() {
        val content = _content.value
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _isGeneratingAi.update { true }
            val result = summarizeNoteUseCase(content)
            result.onSuccess { summary ->
                val newContent = "$content\n\n> **Summary:**\n> $summary"
                _content.value = newContent
            }
            _isGeneratingAi.update { false }
        }
    }

    fun extractAndAddTags() {
        val content = _content.value
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _isGeneratingAi.update { true }
            val result = extractTagsUseCase(content)
            result.onSuccess { extractedTags ->
                ensureNoteSavedEagerly {
                    viewModelScope.launch {
                        extractedTags.forEach { tagName ->
                            addTagToNoteUseCase(currentNoteId, tagName)
                        }
                    }
                }
            }
            _isGeneratingAi.update { false }
        }
    }

    private fun ensureNoteSavedEagerly(onCompletion: () -> Unit) {
        if (!isEditMode) {
            viewModelScope.launch {
                val now = LocalDateTime.now()
                val newNote = Note(
                    id = currentNoteId,
                    title = _title.value,
                    content = _content.value,
                    type = com.ainote.core.model.note.NoteType.TEXT,
                    createdAt = now,
                    updatedAt = now,
                    isPinned = _isPinned.value,
                    isArchived = false
                )
                saveNoteUseCase(newNote)
                isEditMode = true
                currentNote = newNote
                onCompletion()
            }
        } else {
            onCompletion()
        }
    }

    fun addTag(tagName: String) {
        ensureNoteSavedEagerly {
            viewModelScope.launch {
                addTagToNoteUseCase(currentNoteId, tagName)
            }
        }
    }

    fun removeTag(tagId: String) {
        ensureNoteSavedEagerly {
            viewModelScope.launch {
                removeTagFromNoteUseCase(currentNoteId, tagId)
            }
        }
    }

    fun saveNote(onSaveSuccess: () -> Unit) {
        val title = _title.value
        val content = _content.value
        
        // Don't save empty notes
        if (title.isBlank() && content.isBlank()) {
            onSaveSuccess()
            return
        }

        viewModelScope.launch {
            val now = LocalDateTime.now()
            val targetNoteId = if (isEditMode && currentNote != null) {
                val updatedNote = currentNote!!.copy(
                    title = title,
                    content = content,
                    isPinned = _isPinned.value,
                    updatedAt = now
                )
                updateNoteUseCase(updatedNote)
                updatedNote.id
            } else {
                val newId = currentNoteId
                val newNote = Note(
                    id = newId,
                    title = title,
                    content = content,
                    type = com.ainote.core.model.note.NoteType.TEXT,
                    createdAt = now,
                    updatedAt = now,
                    isPinned = _isPinned.value,
                    isArchived = false
                )
                saveNoteUseCase(newNote)
                newId
            }
            
            // Re-process extracted Note Links 
            processNoteLinksUseCase(targetNoteId, content)
            
            onSaveSuccess()
        }
    }
}
