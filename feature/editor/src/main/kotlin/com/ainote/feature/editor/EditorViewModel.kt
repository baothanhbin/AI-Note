package com.ainote.feature.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainote.core.model.note.Note
import com.ainote.domain.usecase.note.GetNoteByIdUseCase
import com.ainote.domain.usecase.note.SaveNoteUseCase
import com.ainote.domain.usecase.note.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

sealed interface EditorUiState {
    data object Loading : EditorUiState
    data class Content(
        val title: String,
        val content: String,
        val isPinned: Boolean
    ) : EditorUiState
}

@HiltViewModel
class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    private val noteId: String? = savedStateHandle["noteId"]
    private var isEditMode = noteId != null
    private var currentNote: Note? = null

    private val _title = MutableStateFlow("")
    private val _content = MutableStateFlow("")
    private val _isPinned = MutableStateFlow(false)
    private val _isLoading = MutableStateFlow(isEditMode)

    val uiState: StateFlow<EditorUiState> = combine(
        _title, _content, _isPinned, _isLoading
    ) { title, content, isPinned, isLoading ->
        if (isLoading) {
            EditorUiState.Loading
        } else {
            EditorUiState.Content(title, content, isPinned)
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
            if (isEditMode && currentNote != null) {
                val updatedNote = currentNote!!.copy(
                    title = title,
                    content = content,
                    isPinned = _isPinned.value,
                    updatedAt = now
                )
                updateNoteUseCase(updatedNote)
            } else {
                val newNote = Note(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    type = com.ainote.core.model.note.NoteType.TEXT,
                    createdAt = now,
                    updatedAt = now,
                    isPinned = _isPinned.value,
                    isArchived = false
                )
                saveNoteUseCase(newNote)
            }
            onSaveSuccess()
        }
    }
}
