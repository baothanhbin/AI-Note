package com.ainote.domain.repository

import com.ainote.core.model.search.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchNotes(query: String): Flow<List<SearchResult>>
}
