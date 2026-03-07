package com.ainote.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query(
        """
        SELECT notes.note_id as noteId, notes.title as title, 
        snippet(notes_fts, -1, '<b>', '</b>', '...', 64) as snippet 
        FROM notes_fts 
        JOIN notes ON notes_fts.note_id = notes.note_id 
        WHERE notes_fts MATCH :query 
        """
    )
    fun searchNotes(query: String): Flow<List<SearchRow>>
}

data class SearchRow(
    val noteId: String,
    val title: String,
    val snippet: String
)
