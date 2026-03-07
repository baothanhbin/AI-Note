package com.ainote.domain.usecase.search

import com.ainote.core.model.search.SearchResult
import com.ainote.data.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchNotesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: String): Flow<List<SearchResult>> {
        if (query.isBlank()) return kotlinx.coroutines.flow.flowOf(emptyList())
        return searchRepository.searchNotes(query)
    }
}
