package com.ainote.data.repository

import com.ainote.core.model.note.NoteLink
import kotlinx.coroutines.flow.Flow

interface LinkRepository {
    suspend fun insertLink(link: NoteLink)
    suspend fun deleteLink(link: NoteLink)
    fun getOutgoingLinks(noteId: String): Flow<List<NoteLink>>
    fun getBacklinks(noteId: String): Flow<List<NoteLink>>
    fun getAllLinks(): Flow<List<NoteLink>>
}
