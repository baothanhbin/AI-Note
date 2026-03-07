package com.ainote.feature.graph

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainote.core.model.graph.GraphData
import com.ainote.domain.usecase.graph.GetGraphDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface GraphUiState {
    data object Loading : GraphUiState
    data class Success(val graphData: GraphData) : GraphUiState
    data class Error(val message: String) : GraphUiState
}

@HiltViewModel
class GraphViewModel @Inject constructor(
    getGraphDataUseCase: GetGraphDataUseCase
) : ViewModel() {

    val uiState: StateFlow<GraphUiState> = getGraphDataUseCase()
        .map { graphData -> GraphUiState.Success(graphData) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GraphUiState.Loading
        )
}
