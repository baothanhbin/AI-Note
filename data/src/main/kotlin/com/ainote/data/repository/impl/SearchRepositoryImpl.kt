package com.ainote.data.repository.impl

import com.ainote.core.database.dao.SearchDao
import com.ainote.core.model.search.SearchResult
import com.ainote.data.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchDao: SearchDao
) : SearchRepository {

    override fun searchNotes(query: String): Flow<List<SearchResult>> {
        val ftsQuery = "*${query}*"
        return searchDao.searchNotes(ftsQuery).map { rows ->
            rows.map { row ->
                SearchResult(
                    noteId = row.noteId,
                    title = row.title,
                    snippet = row.snippet,
                    score = 1.0f, // FTS4 doesn't provide built-in ranking as easily as FTS5, defaulting to 1.0
                    matchedTags = emptyList() // Could be enriched later by cross-referencing tags
                )
            }
        }
    }
}
