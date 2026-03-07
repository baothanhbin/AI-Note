package com.ainote.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainote.domain.usecase.user.GetUseDarkModeUseCase
import com.ainote.domain.usecase.user.GetUseMarkdownPreviewUseCase
import com.ainote.domain.usecase.user.SetUseDarkModeUseCase
import com.ainote.domain.usecase.user.SetUseMarkdownPreviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val useDarkMode: Boolean = false,
    val useMarkdownPreview: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getUseDarkModeUseCase: GetUseDarkModeUseCase,
    getUseMarkdownPreviewUseCase: GetUseMarkdownPreviewUseCase,
    private val setUseDarkModeUseCase: SetUseDarkModeUseCase,
    private val setUseMarkdownPreviewUseCase: SetUseMarkdownPreviewUseCase
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        getUseDarkModeUseCase(),
        getUseMarkdownPreviewUseCase()
    ) { useDarkMode, useMarkdownPreview ->
        SettingsUiState(
            useDarkMode = useDarkMode,
            useMarkdownPreview = useMarkdownPreview
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun toggleDarkMode(useDarkMode: Boolean) {
        viewModelScope.launch {
            setUseDarkModeUseCase(useDarkMode)
        }
    }

    fun toggleMarkdownPreview(useMarkdownPreview: Boolean) {
        viewModelScope.launch {
            setUseMarkdownPreviewUseCase(useMarkdownPreview)
        }
    }
}
