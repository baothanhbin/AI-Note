package com.ainote.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainote.core.model.note.Note
import com.ainote.domain.usecase.note.ArchiveNoteUseCase
import com.ainote.domain.usecase.note.GetAllNotesUseCase
import com.ainote.domain.usecase.note.GetPinnedNotesUseCase
import com.ainote.domain.usecase.note.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val notes: List<Note>,
        val pinnedNotes: List<Note>
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    getAllNotesUseCase: GetAllNotesUseCase,
    getPinnedNotesUseCase: GetPinnedNotesUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val archiveNoteUseCase: ArchiveNoteUseCase
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        getAllNotesUseCase(),
        getPinnedNotesUseCase()
    ) { allNotes, pinnedNotes ->
        val unpinnedNotes = allNotes.filter { !it.isPinned && !it.isArchived }
        HomeUiState.Success(
            notes = unpinnedNotes,
            pinnedNotes = pinnedNotes.filter { !it.isArchived }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState.Loading
    )

    fun togglePin(note: Note) {
        viewModelScope.launch {
            updateNoteUseCase(note.copy(isPinned = !note.isPinned))
        }
    }

    fun archiveNote(noteId: String) {
        viewModelScope.launch {
            archiveNoteUseCase(noteId, true)
        }
    }
}
