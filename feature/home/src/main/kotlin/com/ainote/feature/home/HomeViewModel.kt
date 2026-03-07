package com.ainote.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainote.core.model.note.Note
import com.ainote.domain.usecase.note.ArchiveNoteUseCase
import com.ainote.domain.usecase.note.GetAllNotesUseCase
import com.ainote.domain.usecase.note.GetPinnedNotesUseCase
import com.ainote.domain.usecase.note.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOrder {
    DateDesc,
    DateAsc,
    TitleAsc,
    TitleDesc
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val notes: List<Note>,
        val pinnedNotes: List<Note>,
        val searchQuery: String,
        val sortOrder: SortOrder,
        val isGridView: Boolean
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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DateDesc)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _isGridView = MutableStateFlow(false)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    val uiState: StateFlow<HomeUiState> = combine(
        getAllNotesUseCase(),
        getPinnedNotesUseCase(),
        _searchQuery,
        _sortOrder,
        _isGridView
    ) { allNotes, pinnedNotes, query, order, grid ->
        val unpinned = allNotes.filter { !it.isPinned && !it.isArchived }
        val pinned = pinnedNotes.filter { !it.isArchived }
        val filteredPinned = filterNotes(pinned, query)
        val filteredNotes = filterNotes(unpinned, query)
        val sortedPinned = sortNotes(filteredPinned, order)
        val sortedNotes = sortNotes(filteredNotes, order)
        HomeUiState.Success(
            notes = sortedNotes,
            pinnedNotes = sortedPinned,
            searchQuery = query,
            sortOrder = order,
            isGridView = grid
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState.Loading
    )

    private fun filterNotes(notes: List<Note>, query: String): List<Note> {
        if (query.isBlank()) return notes
        val lower = query.lowercase()
        return notes.filter {
            it.title.lowercase().contains(lower) || it.content.lowercase().contains(lower)
        }
    }

    private fun sortNotes(notes: List<Note>, order: SortOrder): List<Note> {
        return when (order) {
            SortOrder.DateDesc -> notes.sortedByDescending { it.updatedAt }
            SortOrder.DateAsc -> notes.sortedBy { it.updatedAt }
            SortOrder.TitleAsc -> notes.sortedBy { it.title.lowercase() }
            SortOrder.TitleDesc -> notes.sortedByDescending { it.title.lowercase() }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun toggleGridView() {
        _isGridView.value = !_isGridView.value
    }

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
