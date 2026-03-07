package com.ainote.feature.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainote.core.model.note.Note
import com.ainote.core.model.note.Tag
import com.ainote.domain.usecase.tag.GetAllTagsUseCase
import com.ainote.domain.usecase.tag.GetNotesByTagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface TagsUiState {
    data object Loading : TagsUiState
    data class Success(
        val tags: List<Tag>,
        val selectedTag: Tag?,
        val notesByTag: List<Note>
    ) : TagsUiState
}

@HiltViewModel
class TagsViewModel @Inject constructor(
    getAllTagsUseCase: GetAllTagsUseCase,
    private val getNotesByTagUseCase: GetNotesByTagUseCase
) : ViewModel() {

    private val tagsFlow = getAllTagsUseCase()
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _selectedTag = MutableStateFlow<Tag?>(null)
    val selectedTag: StateFlow<Tag?> = _selectedTag.asStateFlow()

    private val _notesByTag = MutableStateFlow<List<Note>>(emptyList())

    val uiState: StateFlow<TagsUiState> = combine(
        tagsFlow,
        _selectedTag,
        _notesByTag
    ) { tags, selected, notes ->
        when {
            tags.isEmpty() && selected == null -> TagsUiState.Success(
                tags = emptyList(),
                selectedTag = null,
                notesByTag = emptyList()
            )
            else -> TagsUiState.Success(
                tags = tags,
                selectedTag = selected,
                notesByTag = notes
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
        initialValue = TagsUiState.Loading
    )

    init {
        viewModelScope.launch {
            _selectedTag.flatMapLatest { tag ->
                if (tag != null) {
                    getNotesByTagUseCase(tag.id)
                } else {
                    flowOf(emptyList())
                }
            }.collect { notes ->
                _notesByTag.update { notes }
            }
        }
    }

    fun selectTag(tag: Tag?) {
        _selectedTag.value = tag
    }
}
